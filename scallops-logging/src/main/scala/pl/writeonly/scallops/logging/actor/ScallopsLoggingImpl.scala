package pl.writeonly.scallops.logging.actor

import pl.writeonly.scallops.logging.common.ScallopsLoggingLike

trait ScallopsLoggingImpl extends ScallopsLoggingLike {

  @volatile lazy val logger = LoggingWrapperImpl(actorSystem, this)

}
