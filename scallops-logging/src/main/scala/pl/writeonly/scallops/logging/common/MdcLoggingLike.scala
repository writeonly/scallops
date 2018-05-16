package pl.writeonly.scallops.logging.common

import akka.event.Logging.MDC

trait MdcLoggingLike {

  def error(mdc: MDC, cause: Throwable, message: String): Unit

  def error(mdc: MDC, message: String): Unit

  def warning(mdc: MDC, message: String): Unit

  def info(mdc: MDC, message: String): Unit

  def debug(mdc: MDC, message: String): Unit

}
