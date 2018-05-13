package pl.writeonly.scala.logging

import akka.actor.ActorRef
import akka.event.Logging._
import akka.event.LoggingAdapter

class LoggingWrapper(logging: LoggingAdapter, actorRef: ActorRef) {

  private def notifyError(message: String, mdc: MDC): Unit =
    actorRef ! Notify.errorNotify(message, mdc)
  private def notifyError(cause: Throwable, message: String, mdc: MDC): Unit =
    actorRef ! Notify.errorNotify(message, mdc)
  private def notifyWarning(message: String, mdc: MDC): Unit =
    actorRef ! Notify.warningNotify(message, mdc)
  private def notifyInfo(message: String, mdc: MDC): Unit =
    actorRef ! Notify.infoNotify(message, mdc)
  private def notifyDebug(message: String, mdc: MDC): Unit =
    actorRef ! Notify.debugNotify(message, mdc)

  private def format(template: String, seq: Any*): String =
    logging.format(template, seq: _*)

  def isErrorEnabled(implicit mdc: MDC): Boolean =
    logging.isErrorEnabled

  def isWarningEnabled(implicit mdc: MDC): Boolean =
    logging.isWarningEnabled

  def isInfoEnabled(implicit mdc: MDC): Boolean =
    logging.isInfoEnabled

  def isDebugEnabled(implicit mdc: MDC): Boolean =
    logging.isDebugEnabled

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
