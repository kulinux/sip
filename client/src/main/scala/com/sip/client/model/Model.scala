package com.sip.client.model


sealed abstract class Sip

case class SipHeader(key: String, value: String) extends Sip


case class SipHeaderResponse(
    head: String,
    status: Int ) extends Sip

case class SHWWWAuthenticate(digest: String, realm: String, nounce: String)
  extends Sip


case class SipMessage(
    head: Sip,
    headers: List[Sip]) extends Sip





