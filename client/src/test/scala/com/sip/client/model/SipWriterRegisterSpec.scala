package com.sip.client.model

import com.sip.client.model.Header._
import com.sip.client.model.Head._
import com.sip.client.model.SipMessages._
import com.sip.client.model.SipMessages.SipRegister
import org.scalatest.{FlatSpec, Matchers}
import Writers._


/**
REGISTER sip:localhost SIP/2.0
Via: SIP/2.0/UDP 192.168.1.132:60026;rport;branch=z9hG4bKPjaO-dZauc9Ep4Mlalnq3m3ZD2HSGJtrXY
Max-Forwards: 70
From: <sip:dos@localhost>;tag=JgsOX.QTyT3cy-ecmWnMpV3PF8IjeUZB
To: <sip:dos@localhost>
Call-ID: dwCHaLd6VNXbcmBHJelyTgPRmFy8iB6p
CSeq: 25762 REGISTER
User-Agent: AGEphone/1.1.0 (Darwin10.13.2; x86_64)
Contact: <sip:dos@192.168.1.132:60026;ob>
Expires: 600
Allow: PRACK, INVITE, ACK, BYE, CANCEL, UPDATE, INFO, NOTIFY, REFER, MESSAGE, OPTIONS
Content-Length:  0
  */



class SipWriterRegisterSpec extends FlatSpec with Matchers {


  "A SipRegister" should "be serialized" in {
    val sipRegister = SipRegister(
      HeaderRegister("localhost"),
      Via("192.168.1.132", 60026, "z9hG4bKPjaO-dZauc9Ep4Mlalnq3m3ZD2HSGJtrXY", None, None),
      MaxForward(70),
      From("dos@localhost", "JgsOX.QTyT3cy-ecmWnMpV3PF8IjeUZB"),
      To("dos@localhost", ""),
      CallId("dwCHaLd6VNXbcmBHJelyTgPRmFy8iB6p"),
      CSeq("25762 REGISTER"),
      UserAgent("AGEphone/1.1.0 (Darwin10.13.2; x86_64)"),
      Contact("dos", "192.168.1.132", 60026),
      Expires(600),
      Allow( Seq("PRACK", "INVITE", "ACK", "BYE", "CANCEL", "UPDATE", "INFO", "NOTIFY", "REFER", "MESSAGE", "OPTIONS") ),
      ContentLength(0)
    )

    val str = SipMarshaller.write(sipRegister)

    println(str)
  }

}
