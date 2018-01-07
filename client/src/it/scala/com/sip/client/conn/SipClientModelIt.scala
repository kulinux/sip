package com.sip.client.conn

import com.sip.client.SipClient
import com.sip.client.model._

import scala.concurrent.Future



object SipClientModelIt extends App {

  import com.sip.client.model.SipRequestMarshallers._
  import com.sip.client.model.marshaller.SipMarshallers._

  def write(sm: SipInviteRequest)(implicit m : SipRequestMarshaller[SipInviteRequest]) = m.marshall(sm)
  def write(sm: SipRegisterRequest)(implicit m : SipRequestMarshaller[SipRegisterRequest]) = m.marshall(sm)

  val sipServer = SipServer("192.168.1.131")
  val whoAmI = WhoAmI("uno@localhost", "uno@192.168.1.131", "192.168.1.131")

  //val register = SipInviteRequest(sipServer, whoAmI)
  val register = SipRegisterRequest(sipServer, whoAmI)

  val sipMessage = write(register)

  implicit val udp : UdpClient = new UdpClient("localhost", 5060)

  val sc = new SipClient()
  val resp : Future[SipMessage] = sc.request(sipMessage)

  Thread.sleep(5000)

}
