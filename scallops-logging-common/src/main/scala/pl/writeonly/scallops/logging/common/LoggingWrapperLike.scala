package pl.writeonly.scallops.logging.common

import akka.event.Logging._
import akka.event.LoggingAdapter

final class LoggingWrapperLike(logging: LoggingAdapter,
                               mdcLogging: MdcLoggingLike) {

  def format(template: String, seq: Any*): String =
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
      mdcLogging.error(mdc, format(template, seq), cause)
    }

  def error(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isErrorEnabled) {
      mdcLogging.error(mdc, format(template, seq))
    }

  def warning(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isWarningEnabled) {
      mdcLogging.warning(mdc, format(template, seq))
    }

  def info(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isInfoEnabled) {
      mdcLogging.info(mdc, format(template, seq))
    }

  def debug(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isDebugEnabled) {
      mdcLogging.debug(mdc, format(template, seq))
    }

}
