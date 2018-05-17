package pl.writeonly.scallops.logging.common

import akka.event.Logging.MDC

trait BackLogging[A] {
  def error(mdc: MDC, message: String, cause: Throwable): A

  def error(mdc: MDC, message: String): A

  def warning(mdc: MDC, message: String): A

  def info(mdc: MDC, message: String): A

  def debug(mdc: MDC, message: String): A
}
