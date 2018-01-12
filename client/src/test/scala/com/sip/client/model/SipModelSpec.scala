package com.sip.client.model

import org.scalatest.{FlatSpec, Matchers}


class SipModelSpec extends FlatSpec with Matchers {

  import com.sip.client.model.SipRequestMarshallers._

  val sipServer = SipServer("192.168.1.131")
  val whoAmI = WhoAmI("uno@localhost", "uno@192.168.1.131", "192.168.1.131")

  def write(sm: SipInviteRequest)(implicit m : SipRequestMarshaller[SipInviteRequest]) = m.marshall(sm)
  def write(sm: SipRegisterRequest)(implicit m : SipRequestMarshaller[SipRegisterRequest]) = m.marshall(sm)

  "A SipRegister" should "serialize" in {
    val register = SipRegisterRequest(sipServer, whoAmI)

    val sipMessage = write(register)

    println(sipMessage)

    sipMessage.head.head should include("REGISTER")


  }

  "A SipInvite" should "serialize" in {

    val register = SipInviteRequest(sipServer, whoAmI)

    val sipMessage = write(register)

    println(sipMessage)

    sipMessage.head.head should include("INVITE")
    /*
    sipMessage.headers.filter(_.key contains("From"))
      .map(_.value).mkString should include("uno@localhost")
      */
  }

}
