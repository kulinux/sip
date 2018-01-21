package com.sip.client.model

import com.sip.client.model.Head.HeaderRegister
import com.sip.client.model.Header._
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

  implicit class Header[T](a: Tuple2[String, T]) {
   def toHeader = s"${a._1}: ${a._2}"
  }

  class SimpleHeader[T](header: String, value: T => String) extends Writer[T] {
    override def write(a: T): String = (header, value(a)).toHeader
  }

  implicit object HeaderRegisterW extends Writer[HeaderRegister] {
    override def write(a: HeaderRegister): String = s"REGISTER sip:${a.server} SIP/2.0"
  }

  implicit val via = new SimpleHeader[Via]("Via", x => s"SIP/2.0/UDP ${x.ip}:${x.port};rport;branch=${x.branch}")
  implicit val maxForward = new SimpleHeader[MaxForward]("MaxForward", _.mf.toString)
  implicit val from = new SimpleHeader[From]("From",  x => s"<sip:dos@localhost>;tag=JgsOX.QTyT3cy-ecmWnMpV3PF8IjeUZB" )
  implicit val to = new SimpleHeader[To]("To",  x => s"<sip:dos@localhost>")
  implicit val callId = new SimpleHeader[CallId]("Call-Id", _.callId )
  implicit val cSeq = new SimpleHeader[CSeq]("CSeq", x => s"25762 REGISTER")
  implicit val userAgent = new SimpleHeader[UserAgent]("User-Agent", x => s"AGEphone/1.1.0 (Darwin10.13.2; x86_64)")
  implicit val contact =  new SimpleHeader[Contact]("Contact", x => "<sip:dos@192.168.1.132:60026;ob>")
  implicit val expires =  new SimpleHeader[Expires]("Expires", _.expires.toString)
  implicit val allow =  new SimpleHeader[Allow]("Allow", _.allows.mkString(","))
  implicit val cl = new SimpleHeader[ContentLength]("Content-Length", _.cl.toString)


  implicit object SipRegisterW extends Writer[SipRegister] {
    override def write(a: SipRegister): String =
      SipMarshaller.write(a.head) + "\n" +
        SipMarshaller.write(a.via) + "\n" +
        SipMarshaller.write(a.maxForwards) + "\n" +
        SipMarshaller.write(a.from) + "\n" +
        SipMarshaller.write(a.to) + "\n" +
        SipMarshaller.write(a.callId) + "\n" +
        SipMarshaller.write(a.cseq) + "\n" +
        SipMarshaller.write(a.userAgent) + "\n" +
        SipMarshaller.write(a.contact) + "\n" +
        SipMarshaller.write(a.expires) + "\n" +
        SipMarshaller.write(a.allow) + "\n" +
        SipMarshaller.write(a.contentLength) + "\n"
  }

}




