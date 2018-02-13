package com.sip.client

import scala.concurrent.ExecutionContext.Implicits.global

object SipClientIt extends App {
  val server = "localhost"
  val user = "dos"
  val pwd = "dos"
  val myIp  = "192.168.1.132"

  val sipClient = new SipClient(
    SipServer(server),
    WhoAmI(user, pwd, myIp, "Scala Client")
  )

  //val rsp = sipClient.register()
  val rsp = sipClient.invite("111@localhost")

  rsp.onComplete( println )

  Thread.sleep(50000)

}
