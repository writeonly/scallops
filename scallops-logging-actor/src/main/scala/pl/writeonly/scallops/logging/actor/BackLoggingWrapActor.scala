package pl.writeonly.scallops.logging.actor

import akka.actor.ActorRef
import akka.event.Logging.MDC
import pl.writeonly.scallops.logging.common.BackLogging

final class BackLoggingWrapActor(actorRef: ActorRef) extends BackLogging[Unit] {
  def error(mdc: MDC, message: String, cause: Throwable): Unit =
    actorRef ! Notify.error(mdc, message, cause)

  def error(mdc: MDC, message: String): Unit =
    actorRef ! Notify.error(mdc, message)

  def warning(mdc: MDC, message: String): Unit =
    actorRef ! Notify.warning(mdc, message)

  def info(mdc: MDC, message: String): Unit =
    actorRef ! Notify.info(mdc, message)

  def debug(mdc: MDC, message: String): Unit =
    actorRef ! Notify.debug(mdc, message)
}
