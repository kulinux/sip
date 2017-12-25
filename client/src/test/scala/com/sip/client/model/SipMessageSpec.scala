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
