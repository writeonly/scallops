package pl.writeonly.scala.logging

import akka.actor.{Actor, DiagnosticActorLogging}
import akka.event.Logging.MDC

final case class Req(work: String, visitorId: Int)

class MdcActorMixin extends Actor with DiagnosticActorLogging {
  var reqId = 0

  override def mdc(currentMessage: Any): MDC = {
    reqId += 1
    val always = Map("requestId" -> reqId)
    val perMessage = currentMessage match {
      case r: Req ⇒ Map("visitorId" -> r.visitorId)
      case _ ⇒ Map()
    }
    always ++ perMessage
  }

  def receive: Receive = {
    case r: Req ⇒ {
      log.info(s"Starting new request: ${r.work}")
    }
  }
}
