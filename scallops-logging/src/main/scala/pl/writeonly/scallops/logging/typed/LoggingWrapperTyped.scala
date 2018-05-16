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

  def error(cause: Throwable, template: String, seq: Any*)(
    implicit mdc: MDC
  ): Unit =
    if (isErrorEnabled) {
      actorRef.error(cause, format(template, seq), mdc)
    }

  def error(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isErrorEnabled) {
      actorRef.error(format(template, seq), mdc)
    }

  def warning(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isWarningEnabled) {
      actorRef.warning(format(template, seq), mdc)
    }

  def info(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isInfoEnabled) {
      actorRef.info(format(template, seq), mdc)
    }

  def debug(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isDebugEnabled) {
      actorRef.debug(format(template, seq), mdc)
    }

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
