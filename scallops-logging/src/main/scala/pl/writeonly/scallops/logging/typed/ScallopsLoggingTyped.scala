package pl.writeonly.scallops.logging.typed

import pl.writeonly.scallops.logging.common.ScallopsLoggingLike

trait ScallopsLoggingTyped extends ScallopsLoggingLike {

  @volatile lazy val logger = LoggingWrapperTyped(actorSystem, this)

}
