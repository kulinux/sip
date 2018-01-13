package com.sip.client.model.marshaller

import com.sip.client.model._

object DefaultMarshaller {
  implicit object BaseSipHeaderMarshaller extends SipMarshaller[BaseSipHeader] {
    override def write(a: BaseSipHeader): String =   ""
  }
}

object SipMarshallers {

  implicit object SipHeadMarshaller extends  SipMarshaller[SipHead] {
    override def write(a: SipHead): String = a.head
  }

  implicit object SipHeaderMarshaller extends SipMarshaller[SipHeader] {
    override def write(a: SipHeader): String =   a.key + ":" + a.value
  }


  implicit object SipHeaderResponseMarshaller extends SipMarshaller[SipHeaderResponse] {
    override def write(a: SipHeaderResponse): String =  s"SIP/2.0 ${a.status} msg"
  }


  implicit object SipMessageMarshaller extends SipMarshaller[SipMessage] {

    import DefaultMarshaller._

    def write[A](a: A)(implicit sh: SipMarshaller[A]) : String = sh.write(a)

    def writeSipHead( sh : BaseSipHead): String = {
     if( sh.isInstanceOf[SipHead] ) return write( sh.asInstanceOf[SipHead] )
     else if( sh.isInstanceOf[SipHeaderResponse] ) return write( sh.asInstanceOf[SipHeaderResponse] )
     else return ""
    }


    override def write(a: SipMessage) = {
      writeSipHead(a.head) +
      a.headers.map( x => write(x) ).mkString("\n") + "\n\n"
    }
  }



}
