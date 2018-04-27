package pl.writeonly.addons.ops

import scala.util.{Failure, Success, Try}

object OptionOps {

  implicit class OptionOps[A](opt: Option[A]) {
    def getOrThrows(exception: => Throwable): A = toTry(exception) match {
      case s: Success[A] => s.get
      case f: Failure[A] => f.get
    }

    def toTry(exception: => Throwable): Try[A] =
      opt
        .map(Success(_))
        .getOrElse(Failure(exception))
  }

}
