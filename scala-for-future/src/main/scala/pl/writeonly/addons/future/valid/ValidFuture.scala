package pl.writeonly.addons.future.valid

import cats.data.Validated.{Invalid, Valid}
import pl.writeonly.addons.future.ToThrowable
import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future.{failed, successful}
import scala.concurrent.{ExecutionContext, Future}

object ValidFuture extends ValidTypes2 with ToThrowable {

  override def inSideOut[A, B](value: Value[A, B])(implicit ec: ExecutionContext): Result[A, B] = value match {
    case Valid(f: Future[B]) => for (a <- f) yield Valid(a)
    case Invalid(a) => Invalid(a) |> successful
  }

  override def getOrFailed[A, B](value: Value[A, B])(implicit ec: ExecutionContext): Future[B] =
    value match {
      case Valid(f: Future[B]) => f
      case Invalid(f) => f |> toThrowable |> failed
    }

  implicit class ValidFutureInSideOut[A, B](value: Value[A, B])(implicit ec: ExecutionContext) {
    def eitherFuture: Result[A, B] =
      ValidFuture.inSideOut(value)(ec)
  }

  implicit class ValidFutureGetOrFailed[A, B](value: Value[A, B])(implicit ec: ExecutionContext) {
    def eitherFuture: Future[B] = ValidFuture.getOrFailed(value)(ec)
  }

}
