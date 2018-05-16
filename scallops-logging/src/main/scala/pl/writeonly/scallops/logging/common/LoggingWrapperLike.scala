package pl.writeonly.scallops.logging.common

import akka.event.Logging._
import akka.event.LoggingAdapter

abstract class LoggingWrapperLike(logging: LoggingAdapter) {

  final def format(template: String, seq: Any*): String =
    logging.format(template, seq: _*)

  final def isErrorEnabled(implicit mdc: MDC): Boolean =
    logging.isErrorEnabled

  final def isWarningEnabled(implicit mdc: MDC): Boolean =
    logging.isWarningEnabled

  final def isInfoEnabled(implicit mdc: MDC): Boolean =
    logging.isInfoEnabled

  final def isDebugEnabled(implicit mdc: MDC): Boolean =
    logging.isDebugEnabled

  final def error(cause: Throwable, template: String, seq: Any*)(
    implicit mdc: MDC
  ): Unit =
    if (isErrorEnabled) {
      error(cause, format(template, seq), mdc)
    }

  final def error(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isErrorEnabled) {
      error(format(template, seq), mdc)
    }

  final def warning(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isWarningEnabled) {
      warning(format(template, seq), mdc)
    }

  final def info(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isInfoEnabled) {
      info(format(template, seq), mdc)
    }

  final def debug(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isDebugEnabled) {
      debug(format(template, seq), mdc)
    }

  protected def error(cause: Throwable, message: String, mdc: MDC): Unit

  protected def error(message: String, mdc: MDC): Unit

  protected def warning(message: String, mdc: MDC): Unit

  protected def info(message: String, mdc: MDC): Unit

  protected def debug(message: String, mdc: MDC): Unit

}
