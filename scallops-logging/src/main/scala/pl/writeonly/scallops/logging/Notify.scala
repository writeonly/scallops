package pl.writeonly.scallops.logging

import akka.event.Logging.MDC

final case class Notify(level: Int,
                        cause: Option[Throwable],
                        message: String,
                        mdc: MDC)

object Notify {
  val ErrorLevel = 4
  val WarningLevel = 3
  val InfoLevel = 2
  val DebugLevel = 1

  def errorNotify(cause: Throwable, message: String, mdc: MDC) =
    Notify(ErrorLevel, Option(cause), message, mdc)
  def errorNotify(message: String, mdc: MDC) =
    Notify(ErrorLevel, None, message, mdc)
  def warningNotify(message: String, mdc: MDC) =
    Notify(WarningLevel, None, message, mdc)
  def infoNotify(message: String, mdc: MDC) =
    Notify(InfoLevel, None, message, mdc)
  def debugNotify(message: String, mdc: MDC) =
    Notify(DebugLevel, None, message, mdc)
}
