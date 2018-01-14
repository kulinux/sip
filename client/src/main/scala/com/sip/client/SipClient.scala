package com.sip.client

import com.sip.client.conn.UdpClient
import com.sip.client.model.{SipMarshaller, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class SipClient {

  def request(req: SipMessage)
    ( implicit udp: UdpClient ) : Future[SipMessage] =  {

    udp.sendAndReceive( SipM.write(req) )
        .map( SipM.read(_) )

    //Future.successful(SipMessage(SipHead(""), List()))
  }

}

