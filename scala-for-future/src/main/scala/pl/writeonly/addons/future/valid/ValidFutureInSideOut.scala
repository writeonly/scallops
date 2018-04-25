package pl.writeonly.addons.future.valid

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future.successful
import scala.concurrent.{ExecutionContext, Future}

object ValidFutureInSideOut extends ValidTypes2 {

  def validFuture[A, B](
    value: Value[A, B]
  )(implicit ec: ExecutionContext): Result[A, B] = value match {
    case Valid(f: Future[B]) => for (a <- f) yield Valid(a)
    case Invalid(a)          => Invalid(a) |> successful
  }

  implicit class FutureEither[A, B](value: Value[A, B])(
    implicit ec: ExecutionContext
  ) {
    def eitherFuture: Result[A, B] =
      ValidFutureInSideOut.validFuture(value)(ec)
  }

}
