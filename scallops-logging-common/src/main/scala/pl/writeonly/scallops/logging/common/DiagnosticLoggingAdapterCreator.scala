package pl.writeonly.scallops.logging.common

import akka.actor.{ActorSystem, ExtendedActorSystem}
import akka.event.{BusLogging, LogSource}

object DiagnosticLoggingAdapterCreator {
  def getLogger(system: ActorSystem, logSource: AnyRef): DLA = {
    val (str, clazz) = LogSource.fromAnyRef(logSource, system)
    val eventStream = system.eventStream
    val loggingFilter = system.asInstanceOf[ExtendedActorSystem].logFilter
    new BusLogging(eventStream, str, clazz, loggingFilter) with DLA
  }

}
