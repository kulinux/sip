package com.sip.client.model


trait Sip

case class SipHead(head: String) extends Sip

case class SipHeader(key: String, value: String) extends Sip

case class SipMessage(
    val head: SipHead,
    headers: List[SipHeader]) extends Sip


trait SipMarshaller[A] {
  def write(a: A) : String
}

object SipMarshallers {

  implicit object SipHeadMarshaller extends  SipMarshaller[SipHead] {
    override def write(a: SipHead): String = a.head
  }

  implicit object SipHeaderMarshaller extends SipMarshaller[SipHeader] {
    override def write(a: SipHeader): String =   a.key + ":" + a.value
  }

  implicit object SipMessageMarshaller extends SipMarshaller[SipMessage] {
    override def write(a: SipMessage) = {
      val sh = implicitly[SipMarshaller[SipHead]]
      val sheader = implicitly[SipMarshaller[SipHead]]

      sh.write(a.head) + "\n" +
        a.headers.map( x => x.key + ":" + x.value ).mkString("\n") +
        "\n\n"

    }
  }
}



class SipInviteRequest {

  var from : String  = ""
  var to : String = ""
  var contact : String = ""
  var allow : String = ""

  def from(str: String) : SipInviteRequest = {
    from = str
    this
  }

  def to(str: String) : SipInviteRequest = {
    to = str
    this
  }

  def contact(str: String) : SipInviteRequest = {
    contact = str
    this
  }

  def allowAll() = {
    allow = "ACK, BYE, CANCEL, INFO, INVITE, NOTIFY, OPTIONS, PRACK, REFER, REGISTER, SUBSCRIBE, UPDATE"
    this
  }

}


class SipResponse {

}
