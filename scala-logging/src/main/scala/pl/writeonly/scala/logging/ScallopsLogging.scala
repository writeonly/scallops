package pl.writeonly.scala.logging

import akka.actor.{ActorRef, ActorSystem, ExtendedActorSystem, Props}
import akka.event._

trait ScallopsLogging {
  protected def actorSystem: ActorSystem

  @volatile private lazy val l: DiagnosticLoggingAdapter =
    ScallopsLogging.getLogger(actorSystem, this)

  @volatile lazy val actorRef: ActorRef =
    actorSystem.actorOf(Props(classOf[LoggingActor], l))

  @volatile lazy val logger = new LoggingWrapper(l, actorRef)

}

object ScallopsLogging {
  def getLogger(system: ActorSystem,
                logSource: AnyRef): DiagnosticLoggingAdapter = {
    val (str, clazz) = LogSource.fromAnyRef(logSource, system)
    new BusLogging(
      system.eventStream,
      str,
      clazz,
      system.asInstanceOf[ExtendedActorSystem].logFilter
    ) with DiagnosticLoggingAdapter
  }
}
