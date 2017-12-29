package com.sip.client.conn

import scala.concurrent.ExecutionContext.Implicits.global

object UdpIt extends App {

  val udp = new UdpClient("localhost", 4444)
  udp.send("hola, udp server!!!!!".getBytes)
    .flatMap( u => udp.receive(str => str.length > 5) )
    .flatMap( u => udp.close() )
    .onComplete( f => println(f))

  Thread.sleep(100000)
}

object UdpSendAndReceiveIt extends App {
  val udp = new UdpClient("localhost", 4444)
  udp.sendAndReceive("hola, udp server")
        .onComplete( x => println(x) )

  Thread.sleep(1000)

}
