package pl.writeonly.scallops.logging.common

import akka.event.Logging._
import akka.event.LoggingAdapter

abstract class LoggingWrapperLike(logging: LoggingAdapter)
    extends MdcLoggingLike {

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
      error(mdc, cause, format(template, seq))
    }

  final def error(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isErrorEnabled) {
      error(mdc, format(template, seq))
    }

  final def warning(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isWarningEnabled) {
      warning(mdc, format(template, seq))
    }

  final def info(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isInfoEnabled) {
      info(mdc, format(template, seq))
    }

  final def debug(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isDebugEnabled) {
      debug(mdc, format(template, seq))
    }

}
