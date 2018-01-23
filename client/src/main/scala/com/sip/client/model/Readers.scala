package com.sip.client.model

import com.sip.client.model.Head.HeaderResponse
import com.sip.client.model.Header._
import com.sip.client.model.SipMessages.{SipRegister, SipResponse}

import scala.util.matching.Regex



trait Read {
  import Readers._
  def read(a: String): SipResponse = {
    val tokens = a.split("\n")
    SipResponse(
      headResponse(tokens.head),
      via(tokens.tail),
      from(tokens.tail),
      to(tokens.tail),
      callId(tokens.tail),
      cSeq(tokens.tail),
      server(tokens.tail),
      allow(tokens.tail),
      supported(tokens.tail),
      wWWAuthenticate(tokens.tail).get,
      contentLength(tokens.tail)
    )
  }
}

object Readers {

  def headResponse(a: String) : HeaderResponse = ???

  def via(str: Seq[String]): Via = ???
  def from(str: Seq[String]): From = ???
  def to(str: Seq[String]): To = ???
  def callId(str: Seq[String]): CallId = ???
  def cSeq(str: Seq[String]): CSeq = ???
  def server(str: Seq[String]): Server = ???
  def allow(str: Seq[String]): Allow = ???
  def supported(str: Seq[String]): Supported = ???


  def header[T](str: Seq[String], head: String, pattern: Regex, function: List[String] => T) : Option[T] = {
    val hl = str.filter( _.startsWith(head) )

    if(hl.isEmpty) return None

    val value = hl(0).split(":")(1)

    val captured = pattern.findAllIn( value ).matchData flatMap ( _.subgroups )

    return Some(function( captured.toList ))


  }


  def wWWAuthenticate(str: Seq[String]): Option[WAuthenticate] = header(
    str,
    "WWW-Authenticate",
    raw"""Digest algorithm=(\w+), realm="(\w+)", nonce="(\w+)"""".r,
    a => WAuthenticate(a(0), a(1), a(2))
  )
  def contentLength(str: Seq[String]): ContentLength = ???


}




