package pl.writeonly.addons.future.valid

import cats.data.Validated.{Invalid, Valid}
import pl.writeonly.addons.future.ToThrowable
import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future.failed
import scala.concurrent.{ExecutionContext, Future}

object ValidFutureGetOrFailed extends ValidTypes2 with ToThrowable {

  def validFuture[A, B](
    value: Value[A, B]
  )(implicit ec: ExecutionContext): Future[B] =
    value match {
      case Valid(f: Future[B]) => f
      case Invalid(f)          => f |> toThrowable |> failed
    }

  implicit class FutureValid[A, B](value: Value[A, B])(
    implicit ec: ExecutionContext
  ) {
    def eitherFuture: Future[B] = ValidFutureGetOrFailed.validFuture(value)(ec)
  }

}
