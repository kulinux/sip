package com.sip.client

package object model {

  case class MarshallException(msg: String) extends Exception

  def marshallExp(msg : String = "") = throw new MarshallException(msg)

}
