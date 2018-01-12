package com.sip.client

import com.sip.client.conn.UdpClient
import com.sip.client.model._
import com.sip.client.model.marshaller.SipMarshaller
import com.sip.client.model.unmarshaller.SipUnmarshallers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class SipClient {

  def request(req: SipMessage)
    ( implicit udp: UdpClient,
      ms: SipMarshaller[SipMessage] ) : Future[SipMessage] =  {

    udp.sendAndReceive( ms.write(req) )
        .map( SipUnmarshallers.parse(_) )

    //Future.successful(SipMessage(SipHead(""), List()))
  }

}

