package pl.writeonly.scallops.logging.actor

import akka.event.Logging.MDC

final case class Notify(level: Int,
                        mdc: MDC,
                        message: String,
                        cause: Option[Throwable])

object Notify {
  val ErrorLevel = 4
  val WarningLevel = 3
  val InfoLevel = 2
  val DebugLevel = 1

  def errorNotify(cause: Throwable, message: String, mdc: MDC) =
    Notify(ErrorLevel, mdc, message, Option(cause))
  def errorNotify(message: String, mdc: MDC) =
    Notify(ErrorLevel, mdc, message, None)
  def warningNotify(message: String, mdc: MDC) =
    Notify(WarningLevel, mdc, message, None)
  def infoNotify(message: String, mdc: MDC) =
    Notify(InfoLevel, mdc, message, None)
  def debugNotify(message: String, mdc: MDC) =
    Notify(DebugLevel, mdc, message, None)
}
