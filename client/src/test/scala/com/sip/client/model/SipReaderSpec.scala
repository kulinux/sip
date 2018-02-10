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
    println(msg)
  }

  "Marshaller" should "read Viaheader" in {
    var header = "Via: SIP/2.0/UDP 192.168.1.132:60026;branch=z9hG4bKPjaO-dZauc9Ep4Mlalnq3m3ZD2HSGJtrXY;received=172.18.0.1;rport=49942"

    val via = Readers.via(Seq(header))
    via.get.port should be (60026)

  }

  "Reader of to" should "read with or without tag" in {
    val withTag = raw"To: <sip:dos@localhost>;tag=as7331f229"
    val withoutTag = raw"To: <sip:dos@localhost>"
    val withoutDiamant = raw"To: sip:dos@localhost"

    Readers.to(Seq(withTag)).get.to should be ("dos@localhost")

    Readers.to(Seq(withoutTag)).get.to should be ("dos@localhost")

    Readers.to(Seq(withoutDiamant)).get.to should be ("dos@localhost")

  }

  "Marshaller" should "read authentication header" in {
    val authStr = """WWW-Authenticate: Digest algorithm=MD5, realm="asterisk", nonce="211fd4cd""""
    val auth = Readers.wWWAuthenticate( Seq(authStr) )

    auth.isDefined should be (true)
    auth.get should have (
      'digest ("MD5"),
      'real ("asterisk"),
      'nonce ("211fd4cd")
    )

  }


}
