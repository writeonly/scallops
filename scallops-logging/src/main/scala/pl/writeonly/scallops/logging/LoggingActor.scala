package pl.writeonly.scallops.logging

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.DiagnosticLoggingAdapter

class LoggingActor(logging: LoggingImpl) extends Actor {

  def receive: Receive = {
    case Notify(Notify.ErrorLevel, Some(cause), message, mdc) =>
      logging.error(cause, message, mdc)

    case Notify(Notify.ErrorLevel, None, message, mdc) =>
      logging.error(message, mdc)

    case Notify(Notify.WarningLevel, None, message, mdc) =>
      logging.warning(message, mdc)

    case Notify(Notify.InfoLevel, None, message, mdc) =>
      logging.info(message, mdc)

    case Notify(Notify.DebugLevel, None, message, mdc) =>
      logging.debug(message, mdc)
  }

}

object LoggingActor {

  def props(
    logging: DiagnosticLoggingAdapter
  )(implicit actorSystem: ActorSystem): ActorRef =
    actorSystem.actorOf(Props(classOf[LoggingActor], new LoggingImpl(logging)))

}
