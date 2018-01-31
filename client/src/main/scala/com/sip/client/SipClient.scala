package com.sip.client

import java.util.UUID

import com.sip.client.conn.UdpClient
import com.sip.client.model.Head.HeaderRegister
import com.sip.client.model.Header._
import com.sip.client.model.{SipMarshaller, Writers}
import com.sip.client.model.SipMessages.{SipRegister, SipResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import Writers._
import com.sip.client.util.Util

import scala.concurrent.Future
import scala.util.{Failure, Success}

class SipClient(server: String) {

  val callId = UUID.randomUUID().toString
  val viaId = UUID.randomUUID().toString

  def register(user: String, userIp: String): Unit =
  {
    val sipRegister = SipRegister(
      HeaderRegister(server),
      Via(userIp, 60026, viaId, None, None),
      MaxForward(70),
      From(user, UUID.randomUUID().toString),
      To(user, ""),
      CallId(callId),
      CSeq("25762 REGISTER"),
      UserAgent("AGEphone/1.1.0 (Darwin10.13.2; x86_64)"),
      Contact(user.split("@")(0), userIp, 60026),
      Expires(600),
      Allow( Seq("PRACK", "INVITE", "ACK", "BYE", "CANCEL", "UPDATE", "INFO", "NOTIFY", "REFER", "MESSAGE", "OPTIONS") ),
      ContentLength(0)
    )

    val udpClient = new UdpClient(server)

    val rsp =
      udpClient.sendAndReceive(SipMarshaller.write(sipRegister))
        .map( SipMarshaller.read )
        .flatMap( x => x match {
            case a: SipResponse if(a.sipResponse.code == 401) =>
              authenticate(udpClient, sipRegister, x, "uno", "uno", "sip:localhost")
            case a: SipResponse if(a.sipResponse.code == 201) => Future(x)
          });


    rsp.onComplete(
      x => x match {
        case Failure(a) => a.printStackTrace()
        case Success(a) => println(a)
      }
    )


    Thread.sleep(10000)
  }

  def authenticate(
                 udpClient: UdpClient,
                 req: SipRegister,
                 rsp: SipResponse,
                 username: String,
                 pwd: String,
                 uri: String): Future[SipResponse] = {
    val wauth = rsp.wWWAuthenticate
    val response = Util.digest(
      wauth.get.real,
      wauth.get.nonce,
      username,
      pwd,
      "REGISTER",
      uri )

    val authorization = Some(Authorization(
      username,
      wauth.get.real,
      wauth.get.nonce,
      uri,
      response,
      "MD5" ))

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


}
