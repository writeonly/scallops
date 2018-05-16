package pl.writeonly.scallops.logging.common

import akka.actor.{ActorSystem, TypedActor, TypedProps}
import akka.event.Logging.MDC
import akka.event.{DiagnosticLoggingAdapter, Logging}

class MdcLoggingImpl(logging: DiagnosticLoggingAdapter) extends MdcLoggingLike {

  def error(mdc: MDC, message: String, cause: Throwable): Unit =
    log(mdc) {
      logging.error(cause, message)
    }

  def error(mdc: MDC, message: String): Unit =
    log(mdc) {
      logging.error(message)
    }

  def warning(mdc: MDC, message: String): Unit =
    log(mdc) {
      logging.warning(message)
    }

  def info(mdc: MDC, message: String): Unit =
    log(mdc) {
      logging.info(message)
    }

  def debug(mdc: MDC, message: String): Unit =
    log(mdc) {
      logging.debug(message)
    }

  private def log(mdc: MDC)(loggingMessage: => Unit): Unit = {
    logging.mdc(mdc)
    loggingMessage
    logging.mdc(Logging.emptyMDC)
  }
}

object MdcLoggingImpl {

  def apply(
    logging: DiagnosticLoggingAdapter
  )(implicit actorSystem: ActorSystem): MdcLoggingLike =
    TypedActor(actorSystem).typedActorOf(
      TypedProps(classOf[MdcLoggingLike], new MdcLoggingImpl(logging)),
      "name"
    )

}
