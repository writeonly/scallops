package pl.writeonly.addons.future.either

import scala.concurrent.Future.successful
import scala.concurrent.{ExecutionContext, Future}

import pl.writeonly.addons.pipe.Pipe._

object EitherFutureInSideOut extends EitherTypes2 {

  def eitherFuture[A, B](
    value: Value[A, B]
  )(implicit ec: ExecutionContext): Result[A, B] =
    value match {
      case Right(f: Future[B]) => for (a <- f) yield Right(a)
      case Left(a)          => Left(a) |> successful
    }

  implicit class FutureEither[A, B](value: Value[A, B])(
    implicit ec: ExecutionContext
  ) {
    def eitherFuture: Result[A, B] =
      EitherFutureInSideOut.eitherFuture(value)(ec)
  }

}
