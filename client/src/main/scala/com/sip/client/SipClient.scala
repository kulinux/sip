package com.sip.client

import com.sip.client.conn.UdpClient
import com.sip.client.model._

import scala.concurrent.ExecutionContext.Implicits.global



import scala.concurrent.Future

object RequestBuilder {
  def invite() = new SipInviteRequest
}



class SipClient {

  def request(req: SipMessage)
    ( implicit udp: UdpClient,
      ms: SipMarshaller[SipMessage] ) : Future[SipMessage] =  {

    udp.send( ms.write(req).getBytes )

    Future.successful(SipMessage(SipHead(""), List()))
  }
}

