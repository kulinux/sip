package com.sip.client.model

import com.sip.client.model.Head.HeaderRegister
import com.sip.client.model.Header.CallId
import com.sip.client.model.SipMessages.{SipRegister, SipResponse}


object SipMarshaller {
  def read(str: String): SipResponse = ???
  def write[A](msg: A)(implicit writer: Writer[A]) = writer.write(msg)

}

trait Writer[A] {
  def write(a: A) : String
}

trait Read {
  def read(a: String): SipResponse
}

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
object Writers {

  implicit class Header(a: Tuple2[String, String]) {
   def toHeader = s"${a._1}: ${a._2}"
  }

  implicit object HeaderRegisterW extends Writer[HeaderRegister] {
    override def write(a: HeaderRegister): String = s"REGISTER sip:${a.server} SIP/2.0"
  }

  implicit object CallIdW extends Writer[CallId] {
    override def write(a: CallId): String = ("Call-Id", a.callId).toHeader
  }

  implicit object SipRegisterW extends Writer[SipRegister] {
    override def write(a: SipRegister): String =
      SipMarshaller.write(a.head) + "\n" +
        SipMarshaller.write(a.callId)
  }

}




