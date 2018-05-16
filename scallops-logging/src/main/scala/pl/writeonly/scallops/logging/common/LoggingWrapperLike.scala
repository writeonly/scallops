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

  def error(cause: Throwable, template: String, seq: Any*)(
    implicit mdc: MDC
  ): Unit

  def error(template: String, seq: Any*)(implicit mdc: MDC): Unit

  def warning(template: String, seq: Any*)(implicit mdc: MDC): Unit

  def info(template: String, seq: Any*)(implicit mdc: MDC): Unit

  def debug(template: String, seq: Any*)(implicit mdc: MDC): Unit

}
