package pl.writeonly.scallops.logging.actor

import akka.actor.ActorSystem
import akka.event.DiagnosticLoggingAdapter
import pl.writeonly.scallops.logging.common.{
  DiagnosticLoggingAdapterCreator,
  LoggingWrapperLike,
  ScallopsLoggingLike
}

trait ScallopsLoggingImpl extends ScallopsLoggingLike {

  @volatile lazy val logger = ScallopsLoggingImpl(actorSystem, this)

}

object ScallopsLoggingImpl {
  def apply(
    logging: DiagnosticLoggingAdapter
  )(implicit actorSystem: ActorSystem): LoggingWrapperLike =
    new LoggingWrapperLike(
      logging,
      new LoggingActorWrapper(LoggingActor.props(logging))
    )

  def apply(system: ActorSystem, logSource: AnyRef)(
    implicit actorSystem: ActorSystem
  ): LoggingWrapperLike =
    apply(DiagnosticLoggingAdapterCreator.getLogger(actorSystem, logSource))
}
