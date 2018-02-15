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

  val sipTrying =
    """SIP/2.0 180 Ringing
      |Via: SIP/2.0/UDP 192.168.1.132:60026;branch=5aed7eee-e441-4222-96d8-34e1f774c63e;received=172.18.0.1;rport=48057
      |From: sip:dos@localhost;tag=df9c75be-15eb-4c17-9be8-4a8e70b26470
      |To: sip:111@localhost;tag=as06e7af15
      |Call-ID: 177a5d24-9985-4c69-b738-95f3d9f7f21d
      |CSeq: 2 INVITE
      |Server: Asterisk PBX certified/11.6-cert16
      |Allow: INVITE, ACK, CANCEL, OPTIONS, BYE, REFER, SUBSCRIBE, NOTIFY, INFO, PUBLISH
      |Supported: replaces, timer
      |Contact: <sip:111@172.18.0.2:5060>
      |Content-Length: 0""".stripMargin

  "Marshaller" should "read msg" in {
    val msg = SipMarshaller.read(msgStr)
    println(msg)
  }

  "Marshaller" should "read trying" in {
    val msg = SipMarshaller.read(sipTrying)
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
