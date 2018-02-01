package com.sip.client.model

import com.sip.client.model.Head.HeaderInvite
import com.sip.client.model.Header._
import com.sip.client.model.SipMessages.{Sdp, SdpItem, SipInvite}
import org.scalatest.{FlatSpec, Matchers}
import Writers._

/*
INVITE sip:22222@localhost SIP/2.0
Via: SIP/2.0/UDP 172.18.0.1:47372;rport;branch=z9hG4bKPjg8SdJ30kcv947CjH5.MmHO98SkNnOo
Max-Forwards: 70
From: sip:dos@localhost;tag=WtDAc4.NK6M99VTfClIcikiAjBeqXpf1
To: sip:22222@localhost
Contact: <sip:dos@172.18.0.1:47372;ob>
Call-ID: eGYhbpgq.V0p-bHCu.M9c3piRgDmwUV-
CSeq: 19037 INVITE
Allow: PRACK, INVITE, ACK, BYE, CANCEL, UPDATE, INFO, NOTIFY, REFER, MESSAGE, OPTIONS
Session-Expires: 180
User-Agent: AGEphone/1.1.0 (Darwin10.13.2; x86_64)
Authorization: Digest username="dos", realm="asterisk", nonce="03e38f3b", uri="sip:222@localhost", response="feb5d5260433ee0c3e0fb47088712c33", algorithm=MD5
Supported: replaces, 100rel, timer
Content-Type: application/sdp
Content-Length:   317

v=0
o=- 3726420969 3726420969 IN IP4 172.18.0.1
s=-
c=IN IP4 172.18.0.1
t=0 0
m=audio 4002 RTP/AVP 3 100 8 0 96
c=IN IP4 172.18.0.1
a=sendrecv
a=rtpmap:3 GSM/8000
a=rtpmap:100 SILK/8000
a=fmtp:100 useinbandfec=0
a=rtpmap:8 PCMA/8000
a=rtpmap:0 PCMU/8000
a=rtpmap:96 telephone-event/8000
a=fmtp:96 0-16
 */

//application/sdp -> https://en.wikipedia.org/wiki/Session_Description_Protocol

class SipWriterInviteSpec extends FlatSpec with Matchers {
  "A SipInvite" should "be serialize" in {
    val sipInvite = SipInvite(
      HeaderInvite("22222@localhost"),
      Via("192.168.1.132", 60026, "z9hG4bKPjaO-dZauc9Ep4Mlalnq3m3ZD2HSGJtrXY", None, None),
      MaxForward(70),
      From("dos@localhost", "JgsOX.QTyT3cy-ecmWnMpV3PF8IjeUZB"),
      To("dos@localhost", ""),
      CallId("dwCHaLd6VNXbcmBHJelyTgPRmFy8iB6p"),
      CSeq("25762 INVITE"),
      UserAgent("AGEphone/1.1.0 (Darwin10.13.2; x86_64)"),
      Contact("dos", "192.168.1.132", 60026),
      Expires(20),
      Allow( Seq("PRACK", "INVITE", "ACK", "BYE", "CANCEL", "UPDATE", "INFO", "NOTIFY", "REFER", "MESSAGE", "OPTIONS") ),
      ContentLength(317),
      Some(Authorization("dos", "asterisk", "nonce", "sip:localhost", "XXXXX", "MD5")),
      Supported(List("replaces 100res", "timer")),
      ContentType("application/sdp"),
      Sdp( List(SdpItem("kkk", "kkk")) )
    )

    val str = SipMarshaller.write(sipInvite)

    println(str)
  }

}
