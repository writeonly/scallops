package pl.writeonly.addons.future.either

import pl.writeonly.addons.future.ToThrowable
import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future.{failed, successful}
import scala.concurrent.{ExecutionContext, Future}

object EitherFuture extends EitherTypes2 with ToThrowable {

  override def getOrFailed[A, B](value: Value[A, B])(implicit ec: ExecutionContext): Future[B] =
    value match {
      case Right(f: Future[B]) => f
      case Left(f)             => f |> toThrowable |> failed
    }

  override def inSideOut[A, B](value: Value[A, B])(implicit ec: ExecutionContext): Result[A, B] =
    value match {
      case Right(f: Future[B]) => for (a <- f) yield Right(a)
      case Left(a)             => Left(a) |> successful
    }

  implicit class FutureEitherInSideOut[A, B](value: Value[A, B])(implicit ec: ExecutionContext) {
    def eitherFuture: Result[A, B] =
      EitherFuture.inSideOut(value)(ec)
  }

  implicit class FutureEitherGetOrFailed[A, B](value: Value[A, B])(implicit ec: ExecutionContext) {
    def eitherFuture: Future[B] =
      EitherFuture.getOrFailed(value)(ec)
  }

}
