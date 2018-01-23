package com.sip.client.model

import com.sip.client.model.SipMessages.SipResponse


object SipMarshaller {
  def read(str: String): SipResponse = ???
  def write[A](msg: A)(implicit writer: Writer[A]) = writer.write(msg)

}




