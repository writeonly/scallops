package pl.writeonly.scallops.logging.actor

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging._
import akka.event.{DiagnosticLoggingAdapter, LoggingAdapter}
import pl.writeonly.scallops.logging.common.{
  DiagnosticLoggingAdapterCreator,
  LoggingWrapperLike
}

class LoggingWrapperImpl(logging: LoggingAdapter, actorRef: ActorRef)
    extends LoggingWrapperLike(logging) {

  def error(mdc: MDC, cause: Throwable, message: String): Unit =
    actorRef ! Notify.errorNotify(cause, message, mdc)

  def error(mdc: MDC, message: String): Unit =
    actorRef ! Notify.errorNotify(message, mdc)

  def warning(mdc: MDC, message: String): Unit =
    actorRef ! Notify.warningNotify(message, mdc)

  def info(mdc: MDC, message: String): Unit =
    actorRef ! Notify.infoNotify(message, mdc)

  def debug(mdc: MDC, message: String): Unit =
    actorRef ! Notify.debugNotify(message, mdc)
}

object LoggingWrapperImpl {
  def apply(
    logging: DiagnosticLoggingAdapter
  )(implicit actorSystem: ActorSystem): LoggingWrapperLike =
    new LoggingWrapperImpl(logging, LoggingActor.props(logging))

  def apply(system: ActorSystem, logSource: AnyRef)(
    implicit actorSystem: ActorSystem
  ): LoggingWrapperLike =
    apply(DiagnosticLoggingAdapterCreator.getLogger(actorSystem, logSource))
}
