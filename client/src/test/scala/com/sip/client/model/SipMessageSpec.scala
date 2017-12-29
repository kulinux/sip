package com.sip.client.model

import org.scalatest.{FlatSpec, Matchers}

object DummyMessage {

  val SipMessageSample = SipMessage(
    SipHead("INVITE sip:user2@server2.com SIP/2.0"),
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
    SipHead("REGISTER sip:localhost SIP/2.0"),
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

class SipMessageSpec extends FlatSpec with Matchers {

  import com.sip.client.model.SipMarshallers._

  /*
INVITE sip:user2@server2.com SIP/2.0
Via: SIP/2.0/UDP pc33.server1.com;branch=z9hG4bK776asdhds Max-Forwards: 70
To: user2 <sip:user2@server2.com>
From: user1 <sip:user1@server1.com>;tag=1928301774
Call-ID: a84b4c76e66710@pc33.server1.com
CSeq: 314159 INVITE
Contact: <sip:user1@pc33.server1.com>
Content-Type: application/sdp
Content-Length: 142
   */

  def write(sm: SipMessage)(implicit m : SipMarshaller[SipMessage]) = m.write(sm)

  "Sip Message" should "serialize" in {
    println( write( DummyMessage.SipMessageSample ))
  }


}
