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

  private def notifyError(message: String, mdc: MDC): Unit =
    actorRef ! Notify.errorNotify(message, mdc)
  private def notifyError(cause: Throwable, message: String, mdc: MDC): Unit =
    actorRef ! Notify.errorNotify(cause, message, mdc)
  private def notifyWarning(message: String, mdc: MDC): Unit =
    actorRef ! Notify.warningNotify(message, mdc)
  private def notifyInfo(message: String, mdc: MDC): Unit =
    actorRef ! Notify.infoNotify(message, mdc)
  private def notifyDebug(message: String, mdc: MDC): Unit =
    actorRef ! Notify.debugNotify(message, mdc)

  def error(cause: Throwable, template: String, seq: Any*)(
    implicit mdc: MDC
  ): Unit =
    if (isErrorEnabled) {
      actorRef ! notifyError(cause, format(template, seq), mdc)
    }

  def error(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isErrorEnabled) {
      actorRef ! notifyError(format(template, seq), mdc)
    }

  def warning(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isWarningEnabled) {
      actorRef ! notifyWarning(format(template, seq), mdc)
    }

  def info(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isInfoEnabled) {
      actorRef ! notifyInfo(format(template, seq), mdc)
    }

  def debug(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isDebugEnabled) {
      actorRef ! notifyDebug(format(template, seq), mdc)
    }

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
