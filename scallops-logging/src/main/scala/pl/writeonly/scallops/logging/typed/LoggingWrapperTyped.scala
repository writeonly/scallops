package pl.writeonly.scallops.logging.typed

import akka.actor.ActorSystem
import akka.event.Logging._
import akka.event.{DiagnosticLoggingAdapter, LoggingAdapter}
import pl.writeonly.scallops.logging.common.{
  DiagnosticLoggingAdapterCreator,
  LoggingImpl,
  LoggingLike,
  LoggingWrapperLike
}

class LoggingWrapperTyped(logging: LoggingAdapter, actorRef: LoggingLike)
    extends LoggingWrapperLike(logging) {

  protected def error(cause: Throwable, message: String, mdc: MDC): Unit =
    actorRef.error(cause, message, mdc)

  protected def error(message: String, mdc: MDC): Unit =
    actorRef.error(message, mdc)

  protected def warning(message: String, mdc: MDC): Unit =
    actorRef.warning(message, mdc)

  protected def info(message: String, mdc: MDC): Unit =
    actorRef.info(message, mdc)

  protected def debug(message: String, mdc: MDC): Unit =
    actorRef.debug(message, mdc)
}

object LoggingWrapperTyped {
  def apply(logging: DiagnosticLoggingAdapter)(
    implicit actorSystem: ActorSystem
  ): LoggingWrapperLike = new LoggingWrapperTyped(logging, LoggingImpl(logging))

  def apply(system: ActorSystem, logSource: AnyRef)(
    implicit actorSystem: ActorSystem
  ): LoggingWrapperLike =
    apply(DiagnosticLoggingAdapterCreator.getLogger(actorSystem, logSource))

}
