package pl.writeonly.scallops.logging.typed

import akka.actor.ActorSystem
import akka.event.Logging._
import akka.event.{DiagnosticLoggingAdapter, LoggingAdapter}
import pl.writeonly.scallops.logging.common.{
  DiagnosticLoggingAdapterCreator,
  MdcLoggingImpl,
  MdcLoggingLike,
  LoggingWrapperLike
}

class LoggingWrapperTyped(logging: LoggingAdapter, actorRef: MdcLoggingLike)
    extends LoggingWrapperLike(logging) {

  def error(mdc: MDC, cause: Throwable, message: String): Unit =
    actorRef.error(mdc, cause, message)

  def error(mdc: MDC, message: String): Unit =
    actorRef.error(mdc, message)

  def warning(mdc: MDC, message: String): Unit =
    actorRef.warning(mdc, message)

  def info(mdc: MDC, message: String): Unit =
    actorRef.info(mdc, message)

  def debug(mdc: MDC, message: String): Unit =
    actorRef.debug(mdc, message)
}

object LoggingWrapperTyped {
  def apply(
    logging: DiagnosticLoggingAdapter
  )(implicit actorSystem: ActorSystem): LoggingWrapperLike =
    new LoggingWrapperTyped(logging, MdcLoggingImpl(logging))

  def apply(system: ActorSystem, logSource: AnyRef)(
    implicit actorSystem: ActorSystem
  ): LoggingWrapperLike =
    apply(DiagnosticLoggingAdapterCreator.getLogger(actorSystem, logSource))

}
