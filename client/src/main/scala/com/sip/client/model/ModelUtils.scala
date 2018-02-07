package com.sip.client.model

import com.sip.client.model.SipMessages.{Sdp, SdpItem}

object ModelUtils {

  def commonSdp(ip: String): Sdp = {
    Sdp(
      List(
        SdpItem("v", "0"),
        SdpItem("o", s"- 3726420969 3726420969 IN IP4 $ip"),
        SdpItem("s", "-"),
        SdpItem("c", "IN IP4 $ip"),
        SdpItem("t", "0 0"),
        SdpItem("m", "audio 4002 RTP/AVP 3 100 8 0 96"),
        SdpItem("c", "IN IP4 $ip"),
        SdpItem("a", "sendrecv"),
        SdpItem("a", "rtpmap:3 GSM/8000"),
        SdpItem("a", "rtpmap:100 SILK/8000"),
        SdpItem("a", "fmtp:100 useinbandfec=0"),
        SdpItem("a", "rtpmap:8 PCMA/8000"),
        SdpItem("a", "rtpmap:0 PCMU/8000"),
        SdpItem("a", "rtpmap:96 telephone-event/8000"),
        SdpItem("a", "fmtp:96 0-16")
      )
    )
  }




}
