package com.sip.client.model

import org.scalatest.{FlatSpec, Matchers}


class SipModelSpec extends FlatSpec with Matchers {

  import com.sip.client.model.SipRequestMarshallers.SipInviteRequestMarshaller

  def write(sm: SipInviteRequest)(implicit m : SipRequestMarshaller[SipInviteRequest]) = m.marshall(sm)

  val sipServer = SipServer("192.168.1.131")
  val whoAmI = WhoAmI("uno@localhost", "uno@192.168.1.131", "192.168.1.131")

  "A SipInvite" should "serialize" in {

    val register = SipInviteRequest(sipServer, whoAmI)

    val sipMessage = write(register)

    println(sipMessage)
  }

}
