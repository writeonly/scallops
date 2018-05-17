package pl.writeonly.scallops.logging.actor

import akka.actor.{ActorRef, ActorSystem, Props}
import pl.writeonly.scallops.logging.common._

trait ScallopsLoggingImpl extends ScallopsLoggingLike {

  @volatile lazy val logger = ScallopsLoggingImpl(this)

}

object ScallopsLoggingImpl {

  def apply(logSource: AnyRef)(implicit system: ActorSystem): FrontLogging =
    loggingWrapperLike(
      DiagnosticLoggingAdapterCreator.getLogger(system, logSource)
    )

  def loggingWrapperLike(dla: DLA)(implicit system: ActorSystem): FrontLogging =
    new FrontLogging(dla, new BackLoggingWrapActor(actorRef(dla)))

  def actorRef(dla: DLA)(implicit system: ActorSystem): ActorRef =
    system.actorOf(Props(classOf[ActorLogging], BackLoggingImpl(dla)))

}
