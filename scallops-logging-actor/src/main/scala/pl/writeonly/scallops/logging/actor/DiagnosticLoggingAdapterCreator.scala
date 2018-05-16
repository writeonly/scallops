package pl.writeonly.scallops.logging.actor

import akka.actor.{ActorSystem, ExtendedActorSystem}
import akka.event.{BusLogging, DiagnosticLoggingAdapter, LogSource}

object DiagnosticLoggingAdapterCreator {
  def getLogger(system: ActorSystem,
                logSource: AnyRef): DiagnosticLoggingAdapter = {
    val (str, clazz) = LogSource.fromAnyRef(logSource, system)
    val eventStream = system.eventStream
    val loggingFilter = system.asInstanceOf[ExtendedActorSystem].logFilter
    new BusLogging(eventStream, str, clazz, loggingFilter)
    with DiagnosticLoggingAdapter
  }

}
