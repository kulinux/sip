package com.sip.client.model



object Head {
  case class HeaderRegister(server: String)
  case class HeaderResponse(code: Int, msg: String)
  case class HeaderInvite(user: String)

}

object Header {

  case class From(from: String, tag: String)
  case class To(to: String, tag: String)
  case class CallId(callId: String)
  case class CSeq(cSeq: Int, method: String)
  case class UserAgent(userAgent: String)
  case class Contact(user: String, ip: String, port: Int)
  case class Expires(expires: Int)
  case class Allow(allows: Seq[String])
  case class ContentLength(var cl: Int)
  case class MaxForward(mf: Int)
  case class Via(ip: String, port: Int, branch: String, received: Option[String], rport: Option[Int])
  case class Server(server: String)
  case class Supported(sp: List[String])
  case class WAuthenticateChallenge(digest: String, real: String, nonce: String)
  case class ContentType(ct: String)

  //Authorization: Digest username="dos", realm="asterisk",
  // nonce="2ce7368e", uri="sip:localhost", response="4ca814b90d4e207880152ee49f78c8e4", alrithm=MD5

  case class Authorization( username: String,
                            real: String,
                            nonce: String,
                            uri: String,
                            response: String,
                            algorithm : String )
}

object SipMessages {
  import Head._
  import Header._


  case class SipResponse(
    sipResponse: HeaderResponse,
    via: Via,
    from: From,
    to: To,
    callId: CallId,
    cSeq: CSeq,
    server: Server,
    allow: Allow,
    supported: Supported,
    wWWAuthenticate: Option[WAuthenticateChallenge],
    contentLength: ContentLength
  )

  trait SipRequest {
    val via: Via
    val maxForwards: MaxForward
    val from: From
    val to: To
    val callId: CallId
    val cseq: CSeq
    val userAgent: UserAgent
    val contact: Contact
    val expires: Expires
    val allow: Allow
    val contentLength: ContentLength
    val authorization: Option[Authorization]
  }

  case class SipRegister (
    head: HeaderRegister,
    via: Via,
    maxForwards: MaxForward,
    from: From,
    to: To,
    callId: CallId,
    cseq: CSeq,
    userAgent: UserAgent,
    contact: Contact,
    expires: Expires,
    allow: Allow,
    contentLength: ContentLength,
    authorization: Option[Authorization] = None
  ) extends SipRequest



  case class SipInvite (
    head: HeaderInvite,
    via: Via,
    maxForwards: MaxForward,
    from: From,
    to: To,
    callId: CallId,
    cseq: CSeq,
    userAgent: UserAgent,
    contact: Contact,
    expires: Expires,
    allow: Allow,
    contentLength: ContentLength,
    authorization: Option[Authorization] = None,
    supported: Supported,
    contentType: ContentType,
    sdp: Sdp
  ) extends SipRequest

  case class SdpItem(key: String, value: String)
  case class Sdp(sdp: List[SdpItem])
}

