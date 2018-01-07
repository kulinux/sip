package com.sip.client.model


trait Sip

trait BaseSipHead {
  def head: String
}

case class SipHeader(key: String, value: String) extends Sip

case class SipHead(head: String) extends BaseSipHead

case class SipHeaderResponse(
    head: String,
    status: Int ) extends BaseSipHead


case class SipMessage(
    head: BaseSipHead,
    headers: List[SipHeader]) extends Sip


trait SipMarshaller[A] {
  def write(a: A) : String
}

object SipMarshallers {

  implicit object SipHeadMarshaller extends  SipMarshaller[BaseSipHead] {
    override def write(a: BaseSipHead): String = a.head
  }

  implicit object SipHeaderMarshaller extends SipMarshaller[SipHeader] {
    override def write(a: SipHeader): String =   a.key + ":" + a.value
  }

  implicit object SipMessageMarshaller extends SipMarshaller[SipMessage] {
    override def write(a: SipMessage) = {
      val sh = implicitly[SipMarshaller[BaseSipHead]]
      val sheader = implicitly[SipMarshaller[BaseSipHead]]

      sh.write(a.head) + "\n" +
        a.headers.map( x => x.key + ": " + x.value ).mkString("\n") +
        "\n\n"

    }
  }

  def parse(a: String): SipMessage = {

    val default = new PartialFunction[String, SipHead] {
      override def isDefinedAt(x: String): Boolean = true
      override def apply(v1: String): SipHead = SipHead(v1)
    }

    val lines = a.split("\n")
    val sipHead = headSipResponse orElse default
    SipMessage(sipHead(lines.head), List())
  }

  def headSipResponse = new PartialFunction[String, SipHeaderResponse] {
    val pattern = raw"SIP/2.0 (\d+) \w+".r

    override def isDefinedAt(x: String): Boolean = x.matches(pattern.regex)

    override def apply(v1: String): SipHeaderResponse = {

      val code = v1 match { case pattern(l) => l.toInt }

      SipHeaderResponse(v1, code)
    }
  }

  def headerPf(str: String) : PartialFunction[String, SipHeader] = new PartialFunction[String, SipHeader] {
    override def isDefinedAt(x: String): Boolean = x.contains(":")
    override def apply(v1: String): SipHeader = {
      val hk = v1.split(":")
      new SipHeader(hk(0), hk(2))
    }
  }


}

