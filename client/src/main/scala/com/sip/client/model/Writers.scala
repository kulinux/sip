package com.sip.client.model

import com.sip.client.model.Head.HeaderRegister
import com.sip.client.model.Header._
import com.sip.client.model.SipMessages.SipRegister


trait Writer[A] {
  def write(a: A) : String
}


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
  implicit val from = new SimpleHeader[From]("From",  x => s"<sip:${x.from}>;tag=${x.tag}" )
  implicit val to = new SimpleHeader[To]("To",  x => s"<sip:${x.to}>")
  implicit val callId = new SimpleHeader[CallId]("Call-Id", _.callId )
  implicit val cSeq = new SimpleHeader[CSeq]("CSeq", x => s"${x.cSeq}")
  implicit val userAgent = new SimpleHeader[UserAgent]("User-Agent", _.userAgent)
  implicit val contact =  new SimpleHeader[Contact]("Contact", x => s"<sip:${x.user}@${x.ip}:${x.port};ob>")
  implicit val expires =  new SimpleHeader[Expires]("Expires", _.expires.toString)
  implicit val allow =  new SimpleHeader[Allow]("Allow", _.allows.mkString(","))
  implicit val cl = new SimpleHeader[ContentLength]("Content-Length", _.cl.toString)
  implicit val auth = new SimpleHeader[Authorization]("Authorization",
    x => s"""Digest username="${x.username}", realm="${x.real}", nonce="${x.nonce}", uri="${x.uri}", response="${x.response}", algorithm=${x.algorithm}""" )


  implicit object SipRegisterW extends Writer[SipRegister] {
    override def write(a: SipRegister): String = {

      var res = SipMarshaller.write(a.head) + "\n" +
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

      if(a.authorization.isDefined) {
        res = res + SipMarshaller.write(a.authorization.get) + "\n"
      }


      res
    }
  }

}




