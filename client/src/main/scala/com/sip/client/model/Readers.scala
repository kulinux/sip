package com.sip.client.model

import com.sip.client.model.Head.HeaderResponse
import com.sip.client.model.Header._
import com.sip.client.model.SipMessages.{SipRegister, SipResponse}

import scala.util.matching.Regex



object Read {
  import Readers._
  def read(a: String): SipResponse = {
    val tokens = a.split("\n")
    SipResponse(
      headResponse(tokens.head),
      via(tokens.tail).get,
      from(tokens.tail).get,
      to(tokens.tail).get,
      callId(tokens.tail).get,
      cSeq(tokens.tail).get,
      server(tokens.tail).get,
      allow(tokens.tail).get,
      supported(tokens.tail).get,
      wWWAuthenticate(tokens.tail),
      contentLength(tokens.tail).get
    )
  }
}

object Readers {

  def headResponse(a: String) : HeaderResponse = {
    val pat = raw"SIP/2.0 (.+) (.+)".r

    a.trim match {
      case pat(a, b) => HeaderResponse(a.toInt, b)
      case _ => HeaderResponse(0, "")
    }

  }



  def header[T](str: Seq[String], head: String, pattern: Regex, function: List[String] => T) : Option[T] = {
    val hl = str.filter( _.startsWith(head) )

    if(hl.isEmpty) return None
    val value = hl(0).split(": ")(1)
    val captured = pattern.findAllIn( value ).matchData flatMap ( _.subgroups )


    return Some(function( captured.toList ))
  }


  def wWWAuthenticate(str: Seq[String]) = header(
    str,
    "WWW-Authenticate",
    raw"""Digest algorithm=(\w+), realm="(\w+)", nonce="(\w+)"""".r,
    a => WAuthenticateChallenge(a(0), a(1), a(2)) )

  def via(str: Seq[String]) = header(
    str,
    "Via",
    raw"""SIP/2.0/UDP (.+):(.+);branch=(.+);received=(.+);rport=(.+)""".r,
    a => Via(a(0), a(1).toInt, a(2), None, None) )

  def from(str: Seq[String]) = header(
    str,
    "From",
    raw"""<?sip:(.+)>?;tag=(\w+)""".r,
    a => From(a(0), a(1)) )

  def to(str: Seq[String]) = header(
    str,
    "To",
    raw"""<?sip:(.+)>?;tag=(\w+)""".r,
    a => To(a(0), a(1)) )

  def callId(str: Seq[String]) = header(
    str,
    "Call-ID",
    raw"""(.+)""".r,
    a => CallId(a(0)) )

  def cSeq(str: Seq[String]) = header(
    str,
    "CSeq",
    raw"""(.+)""".r,
    a => CSeq(a(0)) )

  def server(str: Seq[String]) = header(
    str,
    "Server",
    raw"""(.+)""".r,
    a => Server(a(0)) )

  def allow(str: Seq[String]) = header(
    str,
    "Allow",
    raw"""(.+)""".r,
    a => Allow(a(0).split(", ")) )

  def supported(str: Seq[String]) = header(
    str,
    "Supported",
    raw"""(.+)""".r,
    a => Supported(a(0).split(", ").toList) )

  def contentLength(str: Seq[String]) = header(
    str,
    "Content-Length",
    raw"""(.+)""".r,
    a => ContentLength(a(0).toInt) )


}




