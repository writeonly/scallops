package pl.writeonly.scallops.logging.actor

import akka.actor.Actor
import pl.writeonly.scallops.logging.common.MdcLoggingImpl

final class LoggingActor(logging: MdcLoggingImpl) extends Actor {

  def receive: Receive = {
    case Notify(Notify.ErrorLevel, mdc, message, Some(cause)) =>
      logging.error(mdc, message, cause)

    case Notify(Notify.ErrorLevel, mdc, message, None) =>
      logging.error(mdc, message)

    case Notify(Notify.WarningLevel, mdc, message, None) =>
      logging.warning(mdc, message)

    case Notify(Notify.InfoLevel, mdc, message, None) =>
      logging.info(mdc, message)

    case Notify(Notify.DebugLevel, mdc, message, None) =>
      logging.debug(mdc, message)
  }

}
