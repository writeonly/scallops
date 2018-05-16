package pl.writeonly.scallops.logging.common

import akka.actor.{ActorSystem, TypedActor, TypedProps}
import akka.event.Logging.MDC
import akka.event.{DiagnosticLoggingAdapter, Logging}

class LoggingImpl(logging: DiagnosticLoggingAdapter) extends LoggingLike {

  def error(cause: Throwable, message: String, mdc: MDC): Unit =
    log(mdc) {
      logging.error(cause, message)
    }

  def error(message: String, mdc: MDC): Unit =
    log(mdc) {
      logging.error(message)
    }

  def warning(message: String, mdc: MDC): Unit =
    log(mdc) {
      logging.warning(message)
    }

  def info(message: String, mdc: MDC): Unit =
    log(mdc) {
      logging.info(message)
    }

  def debug(message: String, mdc: MDC): Unit =
    log(mdc) {
      logging.debug(message)
    }

  private def log(mdc: MDC)(loggingMessage: => Unit): Unit = {
    logging.mdc(mdc)
    loggingMessage
    logging.mdc(Logging.emptyMDC)
  }
}

object LoggingImpl {

  def apply(
    logging: DiagnosticLoggingAdapter
  )(implicit actorSystem: ActorSystem): LoggingLike =
    TypedActor(actorSystem).typedActorOf(
      TypedProps(classOf[LoggingLike], new LoggingImpl(logging)),
      "name"
    )

}
