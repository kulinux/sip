package com.sip.client.model

import org.scalatest.{FlatSpec, Matchers}

object DummyMessage {

  val SipMessageSample = SipMessage(
    SipHeader("INVITE sip:user2@server2.com SIP/2.0", ""),
    List(
      SipHeader("Via", "SIP/2.0/UDP pc33.server1.com;branch=z9hG4bK776asdhds Max-Forwards: 70"),
      SipHeader("To", "user2 <sip:user2@server2.com>"),
      SipHeader("From", "user1 <sip:user1@server1.com>;tag=1928301774"),
      SipHeader("Call-ID", "a84b4c76e66710@pc33.server1.com"),
      SipHeader("CSeq", "314159 INVITE"),
      SipHeader("Contact", "<sip:user1@pc33.server1.com>"),
      SipHeader("Content-Type", "application/sdp"),
      SipHeader("Content-Length", "142")
    )
  )

  val SipMessageRegister = SipMessage(
    SipHeader("REGISTER sip:localhost SIP/2.0", ""),
    List(
      SipHeader("Via","SIP/2.0/UDP 192.168.1.131:5060;rport;branch=z9hG4bKPjQFoKOaWo1YxFrUWBYOAUrqRvRqPSBjt"),
      SipHeader("Max-Forwards", "70"),
      SipHeader("From", "<sip:tres@localhost>;tag=mKVbNF0J4ewfsR75PaDqOq2hwET0fe5W"),
      SipHeader("To", "<sip:tres@localhost>"),
      SipHeader("Call-ID", "6L.VRwAZl2RxelodioGGt-Ws42WBxXEW"),
      SipHeader("CSeq","13328 REGISTER"),
      SipHeader("User-Agent", "AGEphone/1.1.0 (Darwin10.13.2; x86_64)"),
      SipHeader("Contact","<sip:tres@192.168.1.131:5060;ob>"),
      SipHeader("Expires","600"),
      SipHeader("Allow", "PRACK, INVITE, ACK, BYE, CANCEL, UPDATE, INFO, NOTIFY, REFER, MESSAGE, OPTIONS"),
      SipHeader("Content-Length", "0")
    )
  )
}

class SipMessageSerializeSpec extends FlatSpec with Matchers {


  "Sip Message" should "serialize" in {
    println( SipM.write( DummyMessage.SipMessageSample ))
  }

}


class SipMessUnSerializeSpec extends FlatSpec with Matchers {
  val message =
    """SIP/2.0 401 Unauthorized
      |Via: SIP/2.0/UDP 192.168.1.131:5060;branch=z9hG4bKPjQFoKOaWo1YxFrUWBYOAUrqRvRqPSBjt;received=172.18.0.1;rport=56390
      |From: <sip:uno@localhost>;tag=mKVbNF0J4ewfsR75PaDqOq2hwET0fe5W
      |To: <sip:uno@localhost>;tag=as529ae190
      |Call-ID: 6L.VRwAZl2RxelodioGGt-Ws42WBxXEW
      |CSeq: 13328 REGISTER
      |Server: Asterisk PBX certified/11.6-cert16
      |Allow: INVITE, ACK, CANCEL, OPTIONS, BYE, REFER, SUBSCRIBE, NOTIFY, INFO, PUBLISH
      |Supported: replaces, timer
      |WWW-Authenticate: Digest algorithm=MD5, realm="asterisk", nonce="7e8a0c48"
      |Content-Length: 0""".stripMargin

  "A message" should "deserialize" in {
    println(message)
    val parsedMsg = SipM.read(message)

    assert(parsedMsg.head.asInstanceOf[SipHeaderResponse].status == 401)

    val msgStr = SipM.write( parsedMsg )
    println(msgStr)

  }

  "A www authenticate" should "deserialized" in {
    val message =
      """WWW-Authenticate: Digest algorithm=MD5, realm="asterisk", nonce="7e8a0c48"""

    val auth = DefaultSipMarshallers.sipWWWAuth.read(
      DefaultSipMarshallers.sipHeaderMarshaller.read(message) )

    println(auth)

    //auth.digest should be ("MD5")
  }
}
