package com.sip.client.model

import org.scalatest.{FlatSpec, Matchers}

class SipReaderSpec extends FlatSpec with Matchers {

  val msgStr =
    """SIP/2.0 401 Unauthorized
      |Via: SIP/2.0/UDP 192.168.1.132:60026;branch=z9hG4bKPjaO-dZauc9Ep4Mlalnq3m3ZD2HSGJtrXY;received=172.18.0.1;rport=49942
      |From: <sip:dos@localhost>;tag=JgsOX.QTyT3cy-ecmWnMpV3PF8IjeUZB
      |To: <sip:dos@localhost>;tag=as7331f229
      |Call-ID: dwCHaLd6VNXbcmBHJelyTgPRmFy8iB6p
      |CSeq: 25762 REGISTER
      |Server: Asterisk PBX certified/11.6-cert16
      |Allow: INVITE, ACK, CANCEL, OPTIONS, BYE, REFER, SUBSCRIBE, NOTIFY, INFO, PUBLISH
      |Supported: replaces, timer
      |WWW-Authenticate: Digest algorithm=MD5, realm="asterisk", nonce="211fd4cd"
      |Content-Length: 0""".stripMargin

  "Marshaller" should "read msg" in {
    val msg = SipMarshaller.read(msgStr)
  }

  "Marshaller" should "read authentication header" in {
    val authStr = """WWW-Authenticate: Digest algorithm=MD5, realm="asterisk", nonce="211fd4cd""""
    val auth = Readers.wWWAuthenticate( Seq(authStr) )
    println(auth)
  }


}
