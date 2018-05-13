package pl.writeonly.scala.logging

import akka.actor.Actor
import akka.event.Logging.MDC
import akka.event.{DiagnosticLoggingAdapter, Logging}

final case class Req(work: String, visitorId: Int)

class LoggingActor(logging: DiagnosticLoggingAdapter) extends Actor {

  def receive: Receive = {
    case Notify(Notify.ErrorLevel, Some(cause), message, mdc) =>
      log(mdc) {
        logging.error(cause, message)
      }

    case Notify(Notify.ErrorLevel, None, message, mdc) =>
      log(mdc) {
        logging.error(message)
      }

    case Notify(Notify.WarningLevel, None, message, mdc) =>
      log(mdc) {
        logging.warning(message)
      }

    case Notify(Notify.WarningLevel, None, message, mdc) =>
      log(mdc) {
        logging.warning(message)
      }

    case Notify(Notify.InfoLevel, None, message, mdc) =>
      log(mdc) {
        logging.info(message)
      }

    case Notify(Notify.DebugLevel, None, message, mdc) =>
      log(mdc) {
        logging.debug(message)
      }
  }

  def log(mdc: MDC)(loggingMessage: => Unit): Unit = {
    logging.mdc(mdc)
    loggingMessage
    logging.mdc(Logging.emptyMDC)
  }
}
