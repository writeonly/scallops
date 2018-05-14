package pl.writeonly.scalaops.ops.mono

import scala.util.{Failure, Success, Try}

trait EitherOps {

  implicit class EitherOps[B, A](value: Either[B, A]) extends ValueOpsLike[A] {
    def toTry: Try[A] = value match {
      case Right(a) => a |> Success[A]
      case Left(b)  => b |> toThrowable[B] |> Failure[A]
    }

    override def mapOrElse[C](f: A => C)(b: C): C = value.map(f).getOrElse(b)
  }

}

object EitherOps extends EitherOps
