package pl.writeonly.scallops.logging.common

import akka.event.Logging
import akka.event.Logging.MDC

final class BackLoggingImpl(dla: DLA) extends BackLogging[Unit] {

  def error(mdc: MDC, message: String, cause: Throwable): Unit =
    log(mdc) {
      dla.error(cause, message)
    }

  def error(mdc: MDC, message: String): Unit =
    log(mdc) {
      dla.error(message)
    }

  def warning(mdc: MDC, message: String): Unit =
    log(mdc) {
      dla.warning(message)
    }

  def info(mdc: MDC, message: String): Unit =
    log(mdc) {
      dla.info(message)
    }

  def debug(mdc: MDC, message: String): Unit =
    log(mdc) {
      dla.debug(message)
    }

  private def log(mdc: MDC)(loggingMessage: => Unit): Unit = {
    dla.mdc(mdc)
    loggingMessage
    dla.mdc(Logging.emptyMDC)
  }
}

object BackLoggingImpl {
  def apply(logging: DLA): BackLogging[Unit] = new BackLoggingImpl(logging)

}
