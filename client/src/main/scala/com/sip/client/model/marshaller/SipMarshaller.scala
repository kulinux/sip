package com.sip.client.model.marshaller

trait SipMarshaller[A] {
  def write(a: A) : String
}
