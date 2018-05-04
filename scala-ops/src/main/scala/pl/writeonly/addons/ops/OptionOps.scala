package pl.writeonly.addons.ops

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait OptionOps {

  implicit class OptionOps[A](opt: Option[A]) extends ValueOpsLike[A] {

    def toTry: Try[A] =
      opt
        .map(Success(_))
        .getOrElse(Failure(toThrowable))
  }

}

object OptionOps extends OptionOps
