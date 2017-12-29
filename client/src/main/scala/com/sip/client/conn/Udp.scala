package com.sip.client.conn

import java.net.{DatagramPacket, DatagramSocket, InetAddress, InetSocketAddress}

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

  def sendAndReceive(data: String) : Future[String] = Future {

    val clientSocket: DatagramSocket = new DatagramSocket
    val IPAddress: InetAddress = this.address.getAddress
    var sendData: Array[Byte] = data.getBytes()
    val receiveData: Array[Byte] = new Array[Byte](1024)

    val sendPacket: DatagramPacket = new DatagramPacket(sendData, sendData.length, IPAddress, address.getPort)
    clientSocket.send(sendPacket)
    val receivePacket: DatagramPacket = new DatagramPacket(receiveData, receiveData.length)
    clientSocket.receive(receivePacket)
    new String(receivePacket.getData, 0, receivePacket.getLength)

  }


}
