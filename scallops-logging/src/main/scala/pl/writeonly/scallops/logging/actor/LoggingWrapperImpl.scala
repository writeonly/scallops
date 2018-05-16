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
