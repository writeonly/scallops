package pl.writeonly.scallops.logging.actor

import akka.event.Logging.MDC
import pl.writeonly.scallops.logging.common.BackLogging

final case class Notify(level: Int,
                        mdc: MDC,
                        message: String,
                        cause: Option[Throwable])

object Notify extends BackLogging[Notify] {
  val ErrorLevel = 4
  val WarningLevel = 3
  val InfoLevel = 2
  val DebugLevel = 1

  def error(mdc: MDC, message: String, cause: Throwable): Notify =
    Notify(ErrorLevel, mdc, message, Option(cause))
  def error(mdc: MDC, message: String): Notify =
    Notify(ErrorLevel, mdc, message, None)
  def warning(mdc: MDC, message: String): Notify =
    Notify(WarningLevel, mdc, message, None)
  def info(mdc: MDC, message: String): Notify =
    Notify(InfoLevel, mdc, message, None)
  def debug(mdc: MDC, message: String): Notify =
    Notify(DebugLevel, mdc, message, None)
}
