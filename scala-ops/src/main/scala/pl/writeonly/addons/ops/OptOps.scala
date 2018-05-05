package pl.writeonly.addons.ops

import scala.util.{Failure, Success, Try}

trait OptOps {

  implicit class OptionOps[A](opt: Option[A]) extends ValueOpsLike[A] {

    def toTry: Try[A] =
      opt
        .map(Success(_))
        .getOrElse(Failure(toThrowable))
  }

}

object OptOps extends OptOps
