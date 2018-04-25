package pl.writeonly.addons.future.either

import pl.writeonly.addons.future.ToThrowable

import scala.concurrent.Future.failed
import scala.concurrent.{ExecutionContext, Future}
import pl.writeonly.addons.pipe.Pipe._

object EitherFutureGetOrFailed extends EitherTypes2 with ToThrowable {

  def eitherFuture[A, B](
    value: Value[A, B]
  )(implicit ec: ExecutionContext): Future[B] =
    value match {
      case Right(f: Future[B]) => f
      case Left(f)             => f |> toThrowable |> failed
    }

  implicit class FutureEither[A, B](value: Value[A, B])(
    implicit ec: ExecutionContext
  ) {
    def eitherFuture: Future[B] =
      EitherFutureGetOrFailed.eitherFuture(value)(ec)
  }

}
