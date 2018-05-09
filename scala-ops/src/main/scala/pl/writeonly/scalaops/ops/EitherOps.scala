package pl.writeonly.scalaops.ops

import scala.util.{Failure, Success, Try}

trait EitherOps {

  implicit class EitherOps[B, A](value: Either[B, A]) extends ValueOpsLike[A] {
    def toTry: Try[A] = value match {
      case Right(a)   => a |> Success[A]
      case Left(b: B) => b |> toThrowable[B] |> Failure[A]
    }
  }

}

object EitherOps extends EitherOps
