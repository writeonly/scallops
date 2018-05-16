package pl.writeonly.scallops.logging

import akka.event.Logging.MDC
import akka.event.{DiagnosticLoggingAdapter, Logging}

trait LoggingLike {

  def error(cause: Throwable, message: String, mdc: MDC): Unit
  def error(message: String, mdc: MDC): Unit

  def warning(message: String, mdc: MDC): Unit

  def info(message: String, mdc: MDC): Unit

  def debug(message: String, mdc: MDC): Unit

}
