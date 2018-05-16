package pl.writeonly.scallops.logging.typed

import akka.actor.{ActorSystem, TypedActor, TypedProps}
import akka.event.DiagnosticLoggingAdapter
import pl.writeonly.scallops.logging.common._

trait ScallopsLoggingTyped extends ScallopsLoggingLike {

  @volatile lazy val logger = ScallopsLoggingTyped(actorSystem, this)

}

object ScallopsLoggingTyped {

  def apply(system: ActorSystem, logSource: AnyRef)(
    implicit actorSystem: ActorSystem
  ): LoggingWrapperLike =
    loggingWrapperLike(
      DiagnosticLoggingAdapterCreator.getLogger(actorSystem, logSource)
    )

  def loggingWrapperLike(
    logging: DiagnosticLoggingAdapter
  )(implicit actorSystem: ActorSystem): LoggingWrapperLike =
    new LoggingWrapperLike(logging, mdcLoggingImpl(logging))

  def mdcLoggingImpl(
    logging: DiagnosticLoggingAdapter
  )(implicit actorSystem: ActorSystem): MdcLoggingLike =
    TypedActor(actorSystem).typedActorOf(
      TypedProps(classOf[MdcLoggingLike], new MdcLoggingImpl(logging)),
      "name"
    )
}
