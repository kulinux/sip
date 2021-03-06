package com.sip.client.conn

import java.net.{DatagramPacket, DatagramSocket, InetAddress, InetSocketAddress}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UdpClient(host: String, port: Int = 5060) {

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

    while( !until.apply(str) ) {
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

    val IPAddress: InetAddress = this.address.getAddress

    var sendData: Array[Byte] = data.getBytes()
    val sendPacket: DatagramPacket = new DatagramPacket(sendData, sendData.length, IPAddress, address.getPort)
    socket.send(sendPacket)


    val receiveData: Array[Byte] = new Array[Byte](1024)
    val receivePacket: DatagramPacket = new DatagramPacket(receiveData, receiveData.length)
    socket.receive(receivePacket)
    val res = new String(receivePacket.getData, 0, receivePacket.getLength)

    println(data)
    println("*******************")
    println(res)

    res

  }

  def receiveMore() = {
    val receiveData: Array[Byte] = new Array[Byte](1024)
    val receivePacket: DatagramPacket = new DatagramPacket(receiveData, receiveData.length)
    socket.receive(receivePacket)
    val res = new String(receivePacket.getData, 0, receivePacket.getLength)
    println(res)
    res
  }


}
