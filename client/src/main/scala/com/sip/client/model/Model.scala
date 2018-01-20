package com.sip.client.model



object Head {
  case class HeaderRegister(server: String)
  case class HeaderResponse(code: Int, msg: String)

}

/*



SIP/2.0 401 Unauthorized
Via: SIP/2.0/UDP 192.168.1.132:60026;branch=z9hG4bKPjaO-dZauc9Ep4Mlalnq3m3ZD2HSGJtrXY;received=172.18.0.1;rport=49942
From: <sip:dos@localhost>;tag=JgsOX.QTyT3cy-ecmWnMpV3PF8IjeUZB
To: <sip:dos@localhost>;tag=as7331f229
Call-ID: dwCHaLd6VNXbcmBHJelyTgPRmFy8iB6p
CSeq: 25762 REGISTER
Server: Asterisk PBX certified/11.6-cert16
Allow: INVITE, ACK, CANCEL, OPTIONS, BYE, REFER, SUBSCRIBE, NOTIFY, INFO, PUBLISH
Supported: replaces, timer
WWW-Authenticate: Digest algorithm=MD5, realm="asterisk", nonce="211fd4cd"
Content-Length: 0
 */

object Header {

  case class MaxForwards(maxForwards: Int)
  case class From(from: String, tag: String)
  case class To(to: String)
  case class CallId(callId: String)
  case class CSeq(cSeq: String)
  case class UserAgent(userAgent: String)
  case class Contact(user: String, ip: String, port: Int)
  case class Expires(expires: Int)
  case class Allow(allows: Seq[String])
  case class ContentLength(cl: Int)
  case class Via(ip: String, port: String, branch: String, received: String, rport: Int)
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
    maxForwards: MaxForwards,
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



/*




 */





