package pl.writeonly.scallops.logging.common

import akka.event.Logging._
import akka.event.LoggingAdapter

final class FrontLogging(adapter: LoggingAdapter, back: BackLogging[Unit]) {

  def format(template: String, seq: Any*): String =
    adapter.format(template, seq: _*)

  def isErrorEnabled(implicit mdc: MDC): Boolean =
    adapter.isErrorEnabled

  def isWarningEnabled(implicit mdc: MDC): Boolean =
    adapter.isWarningEnabled

  def isInfoEnabled(implicit mdc: MDC): Boolean =
    adapter.isInfoEnabled

  def isDebugEnabled(implicit mdc: MDC): Boolean =
    adapter.isDebugEnabled

  def error(cause: Throwable, template: String, seq: Any*)(
    implicit mdc: MDC
  ): Unit =
    if (isErrorEnabled) {
      back.error(mdc, format(template, seq), cause)
    }

  def error(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isErrorEnabled) {
      back.error(mdc, format(template, seq))
    }

  def warning(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isWarningEnabled) {
      back.warning(mdc, format(template, seq))
    }

  def info(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isInfoEnabled) {
      back.info(mdc, format(template, seq))
    }

  def debug(template: String, seq: Any*)(implicit mdc: MDC): Unit =
    if (isDebugEnabled) {
      back.debug(mdc, format(template, seq))
    }

}
