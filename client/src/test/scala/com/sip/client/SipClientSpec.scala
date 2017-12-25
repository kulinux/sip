package com.sip.client

import com.sip.client.conn.UdpClient
import com.sip.client.model.{DummyMessage, SipMessage, SipResponse}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Future

class SipClientSpec extends FlatSpec with Matchers {


  "Sip Client using Request Builder" should "invite" in {
    val req = RequestBuilder
      .invite()
      .from("<sip:10000@127.0.0.1>")
      .to("<sip:101@127.0.0.1>")
      .contact("<sip:10000@127.0.0.1>")
      .allowAll()

    implicit val udp : UdpClient = ???

    val sc = new SipClient()
    //val resp : Future[SipResponse] = sp.request(req)
  }

}
