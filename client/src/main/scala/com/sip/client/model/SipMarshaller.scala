package com.sip.client.model

import com.sip.client.model.SipMessages.SipResponse


object SipMarshaller {
  def read(str: String): SipResponse = Read.read(str)
  def write[A](msg: A)(implicit writer: Writer[A]) = writer.write(msg)

}




