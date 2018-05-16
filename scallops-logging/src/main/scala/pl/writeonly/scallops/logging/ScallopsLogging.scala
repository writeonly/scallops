package pl.writeonly.scallops.logging

import akka.actor.{ActorRef, ActorSystem, ExtendedActorSystem, Props}
import akka.event._

trait ScallopsLogging {
  protected implicit def actorSystem: ActorSystem

  @volatile private lazy val l: DiagnosticLoggingAdapter =
    ScallopsLogging.getLogger(actorSystem, this)

  @volatile lazy val logger = LoggingWrapperImpl(l)

}

object ScallopsLogging {
  def getLogger(system: ActorSystem,
                logSource: AnyRef): DiagnosticLoggingAdapter = {
    val (str, clazz) = LogSource.fromAnyRef(logSource, system)
    val eventStream = system.eventStream
    val loggingFilter = system.asInstanceOf[ExtendedActorSystem].logFilter
    new BusLogging(eventStream, str, clazz, loggingFilter)
    with DiagnosticLoggingAdapter
  }
}
