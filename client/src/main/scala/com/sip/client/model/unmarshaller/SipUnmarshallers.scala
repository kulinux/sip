package com.sip.client.model.unmarshaller

import com.sip.client.model.{SipHead, SipHeader, SipHeaderResponse, SipMessage}

object SipUnmarshallers {

  def parse(a: String): SipMessage = {

    val default = new PartialFunction[String, SipHead] {
      override def isDefinedAt(x: String): Boolean = true
      override def apply(v1: String): SipHead = SipHead(v1)
    }

    val lines = a.split("\n|\r\n")
    val sipHead = headSipResponse orElse default
    SipMessage(sipHead(lines.head),
      lines.tail.toList.map(headerPf(_))
    )
  }

  def headSipResponse = new PartialFunction[String, SipHeaderResponse] {
    val pattern = raw"SIP/2.0 (\d+) \w+".r

    override def isDefinedAt(x: String): Boolean = x.matches(pattern.regex)

    override def apply(v1: String): SipHeaderResponse = {

      val code = v1 match { case pattern(l) => l.toInt }

      SipHeaderResponse(v1, code)
    }
  }

  def headerPf : PartialFunction[String, SipHeader] = new PartialFunction[String, SipHeader] {
    override def isDefinedAt(x: String): Boolean = x.contains(":")
    override def apply(v1: String): SipHeader = {
      val hk = v1.indexOf(":")
      val res = SipHeader(v1.substring(0, hk), v1.substring(hk + 1))
      res
    }
  }


}
