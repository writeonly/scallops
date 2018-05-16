package pl.writeonly.scallops.logging.common

import akka.actor.ActorSystem

trait ScallopsLoggingLike {
  protected implicit def actorSystem: ActorSystem

  def logger: LoggingWrapperLike

}
