package com.sip.client.util

import java.security.MessageDigest
import java.util.Base64
import javax.xml.bind.DatatypeConverter
import javax.xml.bind.annotation.adapters.HexBinaryAdapter

object Util {


  /*
  https://monero.stackexchange.com/questions/3523/help-me-with-digest-authentication-using-java
  HA1=MD5(MD5(username:realm:password):nonce:cnonce) //cnonce is a client nonce
  HA2=MD5(method:digestURI)
  response=MD5(HA1:nonce:HA2)


  A1 as md5("username:realm:password")
  A2 as md5("requestMethod:requestURI")
  El útlimo hash, conocido como "response", md5("A1:nonce:A2")
  */

  def md5(str: String) = {
    DatatypeConverter.printHexBinary( MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"))).toLowerCase()
  }


  def digest(realm: String, serverNonce: String,
          user: String, pwd: String,
          method: String, uri: String) = {


    val ha1 = md5(s"$user:$realm:$pwd")
    val ha2 = md5(s"$method:$uri")

    md5( s"$ha1:$serverNonce:$ha2")

  }


}
