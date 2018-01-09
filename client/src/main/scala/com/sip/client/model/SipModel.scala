package com.sip.client.model

import java.util.UUID

case class WhoAmI(user: String, contact: String, ip: String, port: Int = 5060)
case class SipServer(ip: String)

trait SipRequestMarshaller[A] {
  def marshall(a: A) : SipMessage
}
object SipRequestMarshallers {

  def commonHeader(a: SipRequest) : List[SipHeader] = List(
    SipHeader("Via",s"SIP/2.0/UDP ${a.whoAmI.ip}:${a.whoAmI.port};rport;branch=z9hG4bKPjQFoKOaWo1YxFrUWBYOAUrqRvRqPSBjt"),
    SipHeader("Max-Forwards", "70"),
    SipHeader("From", s"<sip:${a.whoAmI.user}>;tag=mKVbNF0J4ewfsR75PaDqOq2hwET0fe5W"),
    SipHeader("To", s"<sip:${a.whoAmI.user}>"),
    SipHeader("Call-ID", UUID.randomUUID().toString),
    SipHeader("Contact",s"<sip:${a.whoAmI.contact}:${a.whoAmI.port};ob>"),
    SipHeader("Expires","600"),
    SipHeader("Allow", "PRACK, INVITE, ACK, BYE, CANCEL, UPDATE, INFO, NOTIFY, REFER, MESSAGE, OPTIONS"),
    SipHeader("Content-Length", "0")
  )

  implicit object SipInviteRequestMarshaller extends SipRequestMarshaller[SipInviteRequest] {
    override def marshall(a: SipInviteRequest): SipMessage = {
      SipMessage(
        SipHead(s"INVITE sip:${a.sipServer.ip} SIP/2.0"),
        commonHeader(a) :+ SipHeader("CSeq","13328 INVITE")
      )
    }
  }


  implicit object SipRegisterRequestMarshaller extends SipRequestMarshaller[SipRegisterRequest] {
    override def marshall(a: SipRegisterRequest): SipMessage = {
      SipMessage(
        SipHead(s"REGISTER sip:${a.sipServer.ip} SIP/2.0"),
        commonHeader(a) :+ SipHeader("CSeq","13328 REGISTER")
      )
    }
  }

}




trait SipRequest {
  def sipServer: SipServer
  def whoAmI: WhoAmI
}
case class SipInviteRequest(sipServer: SipServer, whoAmI: WhoAmI) extends SipRequest
case class SipRegisterRequest(sipServer: SipServer, whoAmI: WhoAmI) extends SipRequest


class SipResponse {

}

