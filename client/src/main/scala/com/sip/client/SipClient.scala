package com.sip.client

import java.util.UUID

import com.sip.client.conn.UdpClient
import com.sip.client.model.Head.{HeaderInvite, HeaderRegister}
import com.sip.client.model.Header._
import com.sip.client.model.{SipMarshaller, Writers}
import com.sip.client.model.SipMessages.{SipInvite, SipRegister, SipRequest, SipResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import Writers._
import com.sip.client.model.ModelUtils.commonSdp
import com.sip.client.util.Util

import scala.concurrent.Future
import scala.util.{Failure, Success}


case class SipServer(ipServer: String)
case class WhoAmI(user: String, pwd: String, ip: String, userAgent: String)

class SipClient(sipServer: SipServer, whoAmI: WhoAmI) {

  val udpClient = new UdpClient(sipServer.ipServer)

  var sequence = 1

  val callId = UUID.randomUUID().toString
  val viaId = UUID.randomUUID().toString

  var authorization : Option[Authorization] = None

  def invite(to: String): Future[SipResponse] = {

    val sipInvite = SipInvite(
      HeaderInvite(to),
      Via(whoAmI.ip, 60026, viaId, None, None),
      MaxForward(70),
      From(s"${whoAmI.user}@${sipServer.ipServer}", UUID.randomUUID().toString),
      To(s"${to}", ""),
      CallId(callId),
      CSeq(sequence, "INVITE"),
      UserAgent(whoAmI.userAgent),
      Contact(whoAmI.user, whoAmI.ip, 60026),
      Expires(20),
      Allow( Seq("PRACK", "INVITE", "ACK", "BYE", "CANCEL", "UPDATE", "INFO", "NOTIFY", "REFER", "MESSAGE", "OPTIONS") ),
      ContentLength(0),
      authorization,
      Supported(List("replaces 100res", "timer")),
      ContentType("application/sdp"),
      commonSdp(whoAmI.ip)
    )


    val rsp =
      udpClient.sendAndReceive(SipMarshaller.write(sipInvite))
        .map( SipMarshaller.read )
        .flatMap( x => authenticateIfNecessary(sipInvite, x) )


    var afterRing = rsp.map( waitForSomethingInteresting )

    afterRing.onComplete( x => x match {
      case Failure(a) => a.printStackTrace()
      case Success(a) => {
       println(a)
      }
    })
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
      CSeq(sequence, "REGISTER"),
      UserAgent(whoAmI.userAgent),
      Contact(whoAmI.user, whoAmI.ip, 60026),
      Expires(600),
      Allow( Seq("PRACK", "INVITE", "ACK", "BYE", "CANCEL", "UPDATE", "INFO", "NOTIFY", "REFER", "MESSAGE", "OPTIONS") ),
      ContentLength(0)
    )


    val rsp =
      udpClient.sendAndReceive(SipMarshaller.write(sipRegister))
        .map( SipMarshaller.read )
        .flatMap( x => authenticateIfNecessary(sipRegister, x) );


    rsp.onComplete(
      x => x match {
        case Failure(a) => a.printStackTrace()
        case Success(a) => {
          println(a)
        }
      }
    )
    rsp
  }

  def waitForSomethingInteresting(rsp: SipResponse) = {
    var nextMsg = SipMarshaller.read(udpClient.receiveMore())
    while (nextMsg.sipResponse.code.toString.startsWith("1")) {
      nextMsg = SipMarshaller.read(udpClient.receiveMore())
    }
    nextMsg
  }

  def authenticateIfNecessary(sipRequest: SipRequest, sipResponse: SipResponse) = {
    sipResponse match {
      case a: SipResponse if(a.sipResponse.code == 401) =>
        authenticate(sipRequest, a, s"sip:${whoAmI.ip}")
      case a: SipResponse if(a.sipResponse.code == 201) => Future(a)
    }
  }

  def authenticate(
                    req: SipRequest,
                    rsp: SipResponse,
                    uri: String): Future[SipResponse] = {

    val wauth = rsp.wWWAuthenticate

    val authorization = autorizationHeader(uri, req.head.method, wauth.get)


    val authRequest : SipRequest = req match {
      case sr: SipRegister => sr.copy(cseq = CSeq(sr.cseq.cSeq + 1, sr.cseq.method), authorization = authorization )
      case sr: SipInvite => sr.copy(cseq = CSeq(sr.cseq.cSeq + 1, sr.cseq.method), authorization = authorization )
    }


    udpClient.sendAndReceive(SipMarshaller.write(authRequest))
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
