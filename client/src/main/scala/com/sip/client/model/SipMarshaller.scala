package com.sip.client.model

trait SipMarshaller[A] {
  def write(a: Sip) : String
  def read(a: String): Sip
}


trait SipFriendlyHeaderMarshaller[A <: Sip] {
  val head : String
  def write(a: A): SipHeader
  def read : PartialFunction[SipHeader, A]
}

object SipM {
  def write(a: SipMessage) = DefaultSipMarshallers.sipMessageMarsh.write(friendlyWrite(a))
  def read(a: String) = friendlyRead(DefaultSipMarshallers.sipMessageMarsh.read(a))

  val allHeader =
    DefaultSipMarshallers.sipHeaderResponseMarshaller.read orElse
      DefaultSipMarshallers.sipWWWAuth.read orElse
      DefaultSipMarshallers.defaulFriendly.read

  private def friendlyRead(a: SipMessage) : SipMessage =
    SipMessage(
      allHeader(a.head.asInstanceOf[SipHeader]),
      a.headers.map(_.asInstanceOf[SipHeader]).map(allHeader(_))
    )

  private def friendlyWrite(a: SipMessage) : SipMessage = SipMessage(
    DefaultSipMarshallers.sipHeaderResponseMarshaller.write( a.head.asInstanceOf[SipHeaderResponse] ),
    a.headers.map( DefaultSipMarshallers.write(_) )
  )
}

object DefaultSipMarshallers
  extends SipMarshallers
  with SipFriendlyMarshallers


trait SipFriendlyMarshallers {


  def write(a: Sip) : SipHeader = a match {
    case a: SHWWWAuthenticate => sipWWWAuth.write(a)
    case b : SipHeader => b
  }

  implicit def defaulFriendly = new SipFriendlyHeaderMarshaller[SipHeader] {
    val head = ""
    override def write(a: SipHeader) = a

    override def read : PartialFunction[SipHeader, SipHeader] = { case x => x }
  }

  implicit def sipHeaderResponseMarshaller = new SipFriendlyHeaderMarshaller[SipHeaderResponse] {

    val head = ""

    override def write(a: SipHeaderResponse) =
        SipHeader(s"SIP/2.0 ${a.status} msg", "")


    override def read = {
      case x: SipHeader if(x.key.startsWith("SIP/2.0")) => {
        val pattern = raw"SIP/2.0 (\d+) \w+".r
        val code = x.key match {
          case pattern(l) => l.toInt
        }
        SipHeaderResponse(x.key, code)
      }
    }

  }

  implicit def sipWWWAuth = new SipFriendlyHeaderMarshaller[SHWWWAuthenticate] {
    val head = "WWW-Authenticate"

    override def write(a: SHWWWAuthenticate): SipHeader =
      SipHeader("WWW-Authenticate", s"""Digest algorithm=$a.digest, realm="$a.realm", nonce="$a.nounce"""")

    override def read = new PartialFunction[SipHeader, SHWWWAuthenticate] {
      override def isDefinedAt(x: SipHeader): Boolean = x.key.equals("WWW-Authenticate")

      override def apply(v1: SipHeader): SHWWWAuthenticate = {
        var dc = ""
        var realm = ""
        var nounce = ""
        v1.value.split(", ")
          .map(x => (x.split("=")(0).trim, x.split("=")(1)))
          .map(x => x match {
            case ("Digest algorithm", x) => dc = x
            case ("realm", x) => realm = x
            case ("nonce", x) => nounce = x
          })
        SHWWWAuthenticate(dc, realm, nounce)
      }
    }
  }
}

trait SipMarshallers {

  implicit class RichSip[T <: Sip](sip: T) {
    def asString(implicit writer: SipMarshaller[T]) : String = writer.write(sip)
  }


  implicit def sipHeadMarshaller = new SipMarshaller[SipHeader] {
    override def write(a: Sip): String = a match {
      case SipHeader(head, _) => head
      case _ => marshallExp()

    }
    override def read(a: String): SipHeader = new SipHeader(a, "")
  }

  implicit def sipHeaderMarshaller = new SipMarshaller[SipHeader] {
    override def write(a: Sip): String =   a match {
      case SipHeader(key, value) => key + ":" + value
      case _ => marshallExp()
    }
    override def read(v1: String): SipHeader = {
      val hk = v1.indexOf(":")
      val res = SipHeader(v1.substring(0, hk), v1.substring(hk + 1))
      res
    }
  }


  implicit def sipMessageMarsh = new SipMarshaller[SipMessage] {

    def notLikeThisWrite(a: Sip) = a match {
      case a: SipHeader => sipHeaderMarshaller.write(a)
      case a : SipHeader if(a.value.isEmpty) => sipHeadMarshaller.write(a)
      case _ => ""
    }


    override def write(a: Sip): String = a match {
      case SipMessage(head, headers) =>
        notLikeThisWrite(head) + "\n" +
          headers.map(notLikeThisWrite(_)).mkString("\n")
    }
    override def read(a: String): SipMessage = {
      val lines = a.split("\n")
      SipMessage(sipHeadMarshaller.read( lines.head ),
        lines.tail.map(sipHeaderMarshaller.read(_)).toList
      )
    }
  }
}
