package pl.writeonly.scallops.logging.actor

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.event.DiagnosticLoggingAdapter
import pl.writeonly.scallops.logging.common.{
  LoggingWrapperLike,
  MdcLoggingImpl,
  ScallopsLoggingLike
}

trait ScallopsLoggingImpl extends ScallopsLoggingLike {

  @volatile lazy val logger = ScallopsLoggingImpl(actorSystem, this)

}

object ScallopsLoggingImpl {

  def apply(system: ActorSystem, logSource: AnyRef)(
    implicit actorSystem: ActorSystem
  ): LoggingWrapperLike =
    loggingWrapperLike(
      DiagnosticLoggingAdapterCreator.getLogger(actorSystem, logSource)
    )

  def loggingWrapperLike(
    logging: DiagnosticLoggingAdapter
  )(implicit actorSystem: ActorSystem): LoggingWrapperLike =
    new LoggingWrapperLike(logging, new LoggingActorWrapper(actorRef(logging)))

  def actorRef(
    logging: DiagnosticLoggingAdapter
  )(implicit actorSystem: ActorSystem): ActorRef =
    actorSystem.actorOf(
      Props(classOf[LoggingActor], new MdcLoggingImpl(logging))
    )

}
