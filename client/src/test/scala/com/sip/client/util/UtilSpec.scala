package com.sip.client.util

import org.scalatest.{FlatSpec, Matchers}

class UtilSpec extends FlatSpec with Matchers {

  /*

  440397c6 -> c1947f478bf00efa8d34d1ee5857cc47
  */
  "MD5" should "calculate www algorith schema" in {
    val res = Util.digest("asterisk", "440397c6", "dos", "dos", "REGISTER", "sip:localhost")
    println(res)
    res should be ("c1947f478bf00efa8d34d1ee5857cc47")
  }

}
