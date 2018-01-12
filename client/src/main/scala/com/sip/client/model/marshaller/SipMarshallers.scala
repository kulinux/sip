package com.sip.client.model.marshaller

import com.sip.client.model._

object SipMarshallers {

  implicit object SipHeadMarshaller extends  SipMarshaller[SipHead] {
    override def write(a: SipHead): String = a.head
  }

  implicit object SipHeaderMarshaller extends SipMarshaller[SipHeader] {
    override def write(a: SipHeader): String =   a.key + ":" + a.value
  }

  implicit object BaseSipHeaderMarshaller extends SipMarshaller[BaseSipHeader] {
    override def write(a: BaseSipHeader): String =   ""
  }


  implicit object SipMessageMarshaller extends SipMarshaller[SipMessage] {

    def write[A](a: A)(implicit sh: SipMarshaller[A]) : String = sh.write(a)

    def writeSipHead( sh : BaseSipHead): String = {
     if( sh.isInstanceOf[SipHead] )  write( sh.asInstanceOf[SipHead] )
     ""
    }


    override def write(a: SipMessage) = {
      writeSipHead(a.head) +
      a.headers.map( x => write(x) ).mkString("\n") + "\n\n"
    }
  }



}
