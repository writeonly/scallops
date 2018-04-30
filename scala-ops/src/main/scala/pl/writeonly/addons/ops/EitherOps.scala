package pl.writeonly.addons.ops

import scala.util.{Failure, Success, Try}

import pl.writeonly.addons.pipe.Pipe._

object EitherOps {

  def toThrowable(a: Any): Throwable = a match {
    case f: Throwable => f
    case _            => new IllegalStateException(s"$a")
  }

  implicit class EitherOps[B, A](either: Either[B, A]) {
    def toTry(either: Either[B, A]): Try[A] = either match {
      case Right(a) => Success(a)
      case Left(b)  => Failure[A](toThrowable(b))
    }
  }

}
