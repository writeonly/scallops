package pl.writeonly.sons.utils.ops

import scala.util.{Failure, Success, Try}

object OptionOps {

  implicit class OptionOps[A](opt:Option[A]) {
    def toTry(exception: =>Throwable): Try[A] =   opt
      .map(Success(_))
      .getOrElse(Failure(exception))
    def getOrThrows(exception: =>Throwable): A = toTry(exception).get
  }

}
