package pl.writeonly.scallops.logging.actor

import akka.actor.Actor
import pl.writeonly.scallops.logging.common.BackLogging

final class ActorLogging(back: BackLogging[Unit]) extends Actor {

  def receive: Receive = {
    case Notify(Notify.ErrorLevel, mdc, message, Some(cause)) =>
      back.error(mdc, message, cause)

    case Notify(Notify.ErrorLevel, mdc, message, None) =>
      back.error(mdc, message)

    case Notify(Notify.WarningLevel, mdc, message, None) =>
      back.warning(mdc, message)

    case Notify(Notify.InfoLevel, mdc, message, None) =>
      back.info(mdc, message)

    case Notify(Notify.DebugLevel, mdc, message, None) =>
      back.debug(mdc, message)
  }

}
