package pl.writeonly.scallops.logging.common

import akka.event.Logging.MDC

trait LoggingLike {

  def error(cause: Throwable, message: String, mdc: MDC): Unit

  def error(message: String, mdc: MDC): Unit

  def warning(message: String, mdc: MDC): Unit

  def info(message: String, mdc: MDC): Unit

  def debug(message: String, mdc: MDC): Unit

}
