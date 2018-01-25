package com.sip.client

import java.util.UUID

import com.sip.client.conn.UdpClient
import com.sip.client.model.Head.HeaderRegister
import com.sip.client.model.Header._
import com.sip.client.model.{SipMarshaller, Writers}
import com.sip.client.model.SipMessages.SipRegister

import scala.concurrent.ExecutionContext.Implicits.global
import Writers._

class SipClient(server: String) {

  val callId = UUID.randomUUID().toString
  val viaId = UUID.randomUUID().toString

  def register(user: String, userIp: String): Unit =
  {
    val sipRegister = SipRegister(
      HeaderRegister(server),
      Via(userIp, 60026, viaId, None, None),
      MaxForward(70),
      From(user, "JgsOX.QTyT3cy-ecmWnMpV3PF8IjeUZB"),
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


    rsp.onComplete( x => println(x.get) )

    Thread.sleep(1000)


  }


}
