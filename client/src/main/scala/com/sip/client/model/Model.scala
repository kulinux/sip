package com.sip.client.model


trait Sip

trait BaseSipHead {
  def head: String
}

case class SipHeader(key: String, value: String) extends Sip

case class SipHead(head: String) extends BaseSipHead

case class SipHeaderResponse(
    head: String,
    status: Int ) extends BaseSipHead


case class SipMessage(
    head: BaseSipHead,
    headers: List[SipHeader]) extends Sip








