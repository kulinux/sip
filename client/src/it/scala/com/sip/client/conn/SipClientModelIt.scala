package com.sip.client.conn

import com.sip.client.SipClient
import com.sip.client.model._

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global



object SipClientModelIt extends App {

  import com.sip.client.model.SipRequestMarshallers._

  def write(sm: SipInviteRequest)(implicit m : SipRequestMarshaller[SipInviteRequest]) = m.marshall(sm)
  def write(sm: SipRegisterRequest)(implicit m : SipRequestMarshaller[SipRegisterRequest]) = m.marshall(sm)

  val sipServer = SipServer("192.168.1.131")
  val whoAmI = WhoAmI("uno@localhost", "uno@192.168.1.131", "192.168.1.131")

  val register = SipRegisterRequest(sipServer, whoAmI)

  val sipMessage = write(register)

  implicit val udp : UdpClient = new UdpClient("localhost", 5060)

  val sc = new SipClient()
  val resp : Future[SipMessage] = sc.request(sipMessage)

  def authenticate(rsp: SipMessage) : SipMessage = rsp

  resp
    .filter( _.head.asInstanceOf[SipHeaderResponse].status == 401 )
    .map( authenticate(_) )
      .map( x => sc.request(x) )

  println()

  resp.onComplete(println(_))

  Thread.sleep(500000)

}
