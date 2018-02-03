package com.sip.client

import scala.concurrent.ExecutionContext.Implicits.global

object SipClientIt extends App {
  val server = "localhost"
  val user = "uno"
  val pwd = "uno"
  val userIp ="192.168.1.132"
  val myIp  ="localhost"

  val sipClient = new SipClient(
    SipServer(server),
    WhoAmI(user, pwd, myIp, "Scala Client")
  )

  val rsp = sipClient.register()
    .map( x => sipClient.invite("22222@localhost") )

  rsp.onComplete( println )

  Thread.sleep(50000)

}
