package com.sip.client.model



object Head {
  case class HeaderRegister(server: String)
  case class HeaderResponse(code: Int, msg: String)

}

object Header {

  case class From(from: String, tag: String)
  case class To(to: String)
  case class CallId(callId: String)
  case class CSeq(cSeq: String)
  case class UserAgent(userAgent: String)
  case class Contact(user: String, ip: String, port: Int)
  case class Expires(expires: Int)
  case class Allow(allows: Seq[String])
  case class ContentLength(cl: Int)
  case class MaxForward(mf: Int)
  case class Via(ip: String, port: Int, branch: String, received: Option[String], rport: Option[Int])
  case class Server(server: String)
  case class Supported(sp: Seq[String])
  case class WAuthenticate(digest: String, real: String, nonce: String)
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
    wWWAuthenticate: WAuthenticate,
    contentLength: ContentLength
  )

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
    contentLength: ContentLength
  )
}

