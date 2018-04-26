package pl.writeonly.addons.ops

import scala.util.{Failure, Success, Try}

object OptionOps {

  implicit class OptionOps[A](opt: Option[A]) {
    def getOrThrows(exception: => Throwable): A = toTry(exception).get

    def toTry(exception: => Throwable): Try[A] =
      opt
        .map(Success(_))
        .getOrElse(Failure(exception))
  }

}
