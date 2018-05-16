package pl.writeonly.scallops.logging.actor

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging._
import akka.event.{DiagnosticLoggingAdapter, LoggingAdapter}
import pl.writeonly.scallops.logging.common.{
  DiagnosticLoggingAdapterCreator,
  LoggingWrapperLike
}

class LoggingWrapperImpl(logging: LoggingAdapter, actorRef: ActorRef)
    extends LoggingWrapperLike(logging) {

  protected def error(cause: Throwable, message: String, mdc: MDC): Unit =
    actorRef ! Notify.errorNotify(cause, message, mdc)

  protected def error(message: String, mdc: MDC): Unit =
    actorRef ! Notify.errorNotify(message, mdc)

  protected def warning(message: String, mdc: MDC): Unit =
    actorRef ! Notify.warningNotify(message, mdc)

  protected def info(message: String, mdc: MDC): Unit =
    actorRef ! Notify.infoNotify(message, mdc)

  protected def debug(message: String, mdc: MDC): Unit =
    actorRef ! Notify.debugNotify(message, mdc)
}

object LoggingWrapperImpl {
  def apply(
    logging: DiagnosticLoggingAdapter
  )(implicit actorSystem: ActorSystem): LoggingWrapperLike =
    new LoggingWrapperImpl(logging, LoggingActor.props(logging))

  def apply(system: ActorSystem, logSource: AnyRef)(
    implicit actorSystem: ActorSystem
  ): LoggingWrapperLike =
    apply(DiagnosticLoggingAdapterCreator.getLogger(actorSystem, logSource))
}
