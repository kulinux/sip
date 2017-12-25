package com.sip.client.conn

import com.sip.client.SipClient
import com.sip.client.model.{DummyMessage, SipMessage}

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

object SipClientIt extends App {

  import com.sip.client.model.SipMarshallers._


  val req = DummyMessage.SipMessageSample

  implicit val udp : UdpClient = new UdpClient("localhost", 5060)

  val sc = new SipClient()
  val resp : Future[SipMessage] = sc.request(req)

  resp.onComplete( println )

  Thread.sleep(5000)


}
