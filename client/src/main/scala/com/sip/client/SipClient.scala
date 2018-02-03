package com.sip.client

import java.util.UUID

import com.sip.client.conn.UdpClient
import com.sip.client.model.Head.{HeaderInvite, HeaderRegister}
import com.sip.client.model.Header._
import com.sip.client.model.{SipMarshaller, Writers}
import com.sip.client.model.SipMessages.{SipInvite, SipRegister, SipResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import Writers._
import com.sip.client.model.ModelUtils.commonSdp
import com.sip.client.util.Util

import scala.concurrent.Future
import scala.util.{Failure, Success}


case class SipServer(ipServer: String)
case class WhoAmI(user: String, pwd: String, ip: String, userAgent: String)

class SipClient(sipServer: SipServer, whoAmI: WhoAmI) {

  val callId = UUID.randomUUID().toString
  val viaId = UUID.randomUUID().toString

  var authorization : Option[Authorization] = None

  def invite(to: String): Future[SipResponse] = {

    val sipInvite = SipInvite(
      HeaderInvite(to),
      Via(whoAmI.ip, 60026, viaId, None, None),
      MaxForward(70),
      From(whoAmI.user, UUID.randomUUID().toString),
      To(s"${whoAmI.user}@${whoAmI.ip}", ""),
      CallId(callId),
      CSeq("25762 INVITE"),
      UserAgent(whoAmI.userAgent),
      Contact("dos", "192.168.1.132", 60026),
      Expires(20),
      Allow( Seq("PRACK", "INVITE", "ACK", "BYE", "CANCEL", "UPDATE", "INFO", "NOTIFY", "REFER", "MESSAGE", "OPTIONS") ),
      ContentLength(0),
      authorization,
      Supported(List("replaces 100res", "timer")),
      ContentType("application/sdp"),
      commonSdp("172.18.0.1")
    )

    val udpClient = new UdpClient(sipServer.ipServer)

    val rsp =
      udpClient.sendAndReceive(SipMarshaller.write(sipInvite))
        .map( SipMarshaller.read )

    rsp.onComplete(
      x => x match {
        case Failure(a) => a.printStackTrace()
        case Success(a) => println(a)
      }
    )
    rsp

  }

  def register(): Future[SipResponse] =
  {
    val sipRegister = SipRegister(
      HeaderRegister(sipServer.ipServer),
      Via(whoAmI.ip, 60026, viaId, None, None),
      MaxForward(70),
      From(whoAmI.user, UUID.randomUUID().toString),
      To(s"${whoAmI.user}@${whoAmI.ip}", ""),
      CallId(callId),
      CSeq("25762 REGISTER"),
      UserAgent(whoAmI.userAgent),
      Contact(whoAmI.user, whoAmI.ip, 60026),
      Expires(600),
      Allow( Seq("PRACK", "INVITE", "ACK", "BYE", "CANCEL", "UPDATE", "INFO", "NOTIFY", "REFER", "MESSAGE", "OPTIONS") ),
      ContentLength(0)
    )

    val udpClient = new UdpClient(sipServer.ipServer)

    val rsp =
      udpClient.sendAndReceive(SipMarshaller.write(sipRegister))
        .map( SipMarshaller.read )
        .flatMap( x => x match {
            case a: SipResponse if(a.sipResponse.code == 401) =>
              authenticate(udpClient, sipRegister, x, whoAmI.user, whoAmI.pwd, s"sip:${whoAmI.ip}")
            case a: SipResponse if(a.sipResponse.code == 201) => Future(x)
          });


    rsp.onComplete(
      x => x match {
        case Failure(a) => a.printStackTrace()
        case Success(a) => println(a)
      }
    )
    rsp
  }

  def authenticate(
                 udpClient: UdpClient,
                 req: SipRegister,
                 rsp: SipResponse,
                 username: String,
                 pwd: String,
                 uri: String): Future[SipResponse] = {

    val wauth = rsp.wWWAuthenticate

    val authorization = autorizationHeader(uri, "REGISTER", wauth.get)

    val sipRegister = SipRegister(
      req.head,
      req.via,
      req.maxForwards,
      req.from,
      req.to,
      req.callId,
      req.cseq,
      req.userAgent,
      req.contact,
      req.expires,
      req.allow,
      req.contentLength,
      authorization )

    udpClient.sendAndReceive(SipMarshaller.write(sipRegister))
      .map( SipMarshaller.read )
  }


  private def autorizationHeader(uri: String, method: String, wauth: WAuthenticateChallenge) = {
    val response = Util.digest(
      wauth.real,
      wauth.nonce,
      whoAmI.user,
      whoAmI.pwd,
      method,
      uri)

    Some(Authorization(
      whoAmI.user,
      wauth.real,
      wauth.nonce,
      uri,
      response,
      "MD5"))
  }
}
