package pl.writeonly.scallops.logging.typed

import akka.actor.ActorSystem
import akka.event.DiagnosticLoggingAdapter
import pl.writeonly.scallops.logging.common.{
  DiagnosticLoggingAdapterCreator,
  LoggingWrapperLike,
  MdcLoggingImpl
}

object LoggingWrapperTyped {
  def apply(
    logging: DiagnosticLoggingAdapter
  )(implicit actorSystem: ActorSystem): LoggingWrapperLike =
    new LoggingWrapperLike(logging, MdcLoggingImpl(logging))

  def apply(system: ActorSystem, logSource: AnyRef)(
    implicit actorSystem: ActorSystem
  ): LoggingWrapperLike =
    apply(DiagnosticLoggingAdapterCreator.getLogger(actorSystem, logSource))

}
