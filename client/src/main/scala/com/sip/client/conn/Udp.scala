package com.sip.client.conn

import java.net.{DatagramPacket, DatagramSocket, InetSocketAddress}


import scala.concurrent.ExecutionContext.Implicits.global


import scala.concurrent.Future

class UdpClient(host: String, port: Int) {

  val socket = new DatagramSocket()
  val address = new InetSocketAddress(host, port)


  def close() : Future[Unit] = Future {
    socket.close()
    Future.successful()
  }

  def receive(until : String => Boolean) = Future {
    var str = ""

    val receiveData = new Array[Byte](50)
    val receivePacket: DatagramPacket = new DatagramPacket(receiveData, receiveData.length)

    while( until.apply( str ) == false ) {
      socket.receive(receivePacket)
      str = str +
        receivePacket.getData().take( receivePacket.getLength ).map(_.toChar).mkString
      println(str)
    }
    str
  }

  def send(payload: Array[Byte]) : Future[Unit] = Future {

    val packet = new DatagramPacket(payload, payload.length, address)

    socket.send(packet)
    Future.successful()
  }


}
