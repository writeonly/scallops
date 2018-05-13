package pl.writeonly.scala.logging

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.event.Logging

trait ScallopsLogging {
  protected def actorSystem: ActorSystem

  @volatile private lazy val l = Logging.getLogger(actorSystem, this)

  @volatile lazy val actorRef: ActorRef =
    actorSystem.actorOf(Props(classOf[LoggingActor], l))

  @volatile lazy val logger = new LoggingWrapper(l, actorRef)
}
