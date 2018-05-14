package pl.writeonly.scalaops.ops.mono

import scala.util.{Failure, Success, Try}

trait OptOps {

  implicit class OptionOps[A](value: Option[A]) extends ValueOpsLike[A] {

    def toTry: Try[A] =
      value
        .map(Success(_))
        .getOrElse(Failure(toThrowable))

    override def mapOrElse[B](f: A => B)(b: B): B = value.map(f).getOrElse(b)

  }

}

object OptOps extends OptOps
