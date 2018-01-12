package com.sip.client.model


trait Sip

trait BaseSipHead {
  def head: String
}

trait BaseSipHeader

case class SipHeader(key: String, value: String) extends BaseSipHeader

case class SipHead(head: String) extends BaseSipHead

case class SipHeaderResponse(
    head: String,
    status: Int ) extends BaseSipHead

case class SHWWWAuthenticate(digest: String, realm: String, nounce: String)
  extends BaseSipHeader


case class SipMessage(
    head: BaseSipHead,
    headers: List[BaseSipHeader]) extends Sip





