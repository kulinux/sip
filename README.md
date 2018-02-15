# sip
Sip scala client


```
  val server = "localhost"
  val user = "dos"
  val pwd = "dos"
  val myIp  = "192.168.1.132"

  val sipClient = new SipClient(
    SipServer(server),
    WhoAmI(user, pwd, myIp, "Scala Client")
  )

  val rsp = sipClient.invite("111@localhost")

  rsp.onComplete( println )
```
