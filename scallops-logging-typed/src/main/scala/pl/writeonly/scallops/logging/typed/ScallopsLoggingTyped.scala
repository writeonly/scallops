package pl.writeonly.scallops.logging.typed

import akka.actor.{ActorSystem, TypedActor, TypedProps}
import pl.writeonly.scallops.logging.common._

trait ScallopsLoggingTyped extends ScallopsLoggingLike {

  @volatile lazy val logger = ScallopsLoggingTyped(this)

}

object ScallopsLoggingTyped {

  def apply(logSource: AnyRef)(implicit system: ActorSystem): FrontLogging =
    loggingWrapperLike(
      DiagnosticLoggingAdapterCreator.getLogger(system, logSource)
    )

  def loggingWrapperLike(dla: DLA)(implicit system: ActorSystem): FrontLogging =
    new FrontLogging(dla, mdcLoggingImpl(dla))

  def mdcLoggingImpl(
    dla: DLA
  )(implicit system: ActorSystem): BackLogging[Unit] =
    TypedActor(system).typedActorOf(
      TypedProps(classOf[BackLogging[Unit]], BackLoggingImpl(dla)),
      "name"
    )
}
