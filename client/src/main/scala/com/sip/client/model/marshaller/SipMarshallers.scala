package com.sip.client.model.marshaller

import com.sip.client.model.{BaseSipHead, SipHeader, SipMessage}

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



}
