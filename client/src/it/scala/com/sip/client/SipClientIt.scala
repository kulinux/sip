package com.sip.client

object SipClientIt extends App {
  val server = "localhost"
  val user = "uno@localhost"
  val userIp ="192.168.1.132"

  val sipClient = new SipClient(server)
  sipClient.register(user, userIp)

}
