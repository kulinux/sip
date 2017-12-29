package com.sip.client.model

case class WhoAmI(user: String, contact: String, ip: String, port: Int = 5060)
case class SipServer(ip: String)

trait SipRequestMarshaller[A] {
  def marshall(a: A) : SipMessage
}
object SipRequestMarshallers {

  implicit object SipInviteRequestMarshaller extends SipRequestMarshaller[SipInviteRequest] {
    override def marshall(a: SipInviteRequest): SipMessage = {
      SipMessage(
        SipHead(s"REGISTER sip:${a.sipServer.ip} SIP/2.0"),
        List(
          SipHeader("Via",s"SIP/2.0/UDP ${a.whoAmI.ip}:${a.whoAmI.port};rport;branch=z9hG4bKPjQFoKOaWo1YxFrUWBYOAUrqRvRqPSBjt"),
          SipHeader("Max-Forwards", "70"),
          SipHeader("From", "<sip:tres@localhost>;tag=mKVbNF0J4ewfsR75PaDqOq2hwET0fe5W"),
          SipHeader("To", s"<sip:${a.whoAmI.user}"),
          SipHeader("Call-ID", "6L.VRwAZl2RxelodioGGt-Ws42WBxXEW"),
          SipHeader("CSeq","13328 REGISTER"),
          SipHeader("User-Agent", "AGEphone/1.1.0 (Darwin10.13.2; x86_64)"),
          SipHeader("Contact",s"<sip:${a.whoAmI.contact}:${a.whoAmI.port};ob>"),
          SipHeader("Expires","600"),
          SipHeader("Allow", "PRACK, INVITE, ACK, BYE, CANCEL, UPDATE, INFO, NOTIFY, REFER, MESSAGE, OPTIONS"),
          SipHeader("Content-Length", "0")
        )
      )
    }
  }

}





case class SipInviteRequest(sipServer: SipServer, whoAmI: WhoAmI)


class SipResponse {

}

