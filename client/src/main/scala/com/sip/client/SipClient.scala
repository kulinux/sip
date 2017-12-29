package com.sip.client

import com.sip.client.conn.UdpClient
import com.sip.client.model._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class SipClient {

  def request(req: SipMessage)
    ( implicit udp: UdpClient,
      ms: SipMarshaller[SipMessage] ) : Future[SipMessage] =  {

    udp.sendAndReceive( ms.write(req) )
        .onComplete( x => println(x) )

    Future.successful(SipMessage(SipHead(""), List()))
  }
}

