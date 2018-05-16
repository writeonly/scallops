package pl.writeonly.scallops.logging

import akka.actor.ActorSystem
import akka.event._

trait ScallopsLoggingTyped {
  protected implicit def actorSystem: ActorSystem

  @volatile private lazy val l: DiagnosticLoggingAdapter =
    ScallopsLogging.getLogger(actorSystem, this)
  @volatile lazy val logger = LoggingWrapperTyped(l)

}
