package pl.writeonly.addons.future.either

import pl.writeonly.addons.future.{Utils, Types2}
import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future.{failed, successful}
import scala.concurrent.{ExecutionContext, Future}

object EitherFuture extends Types2 with Utils {
  override type Value[A, B] = Either[A, Future[B]]
  override type Result[A, B] = Future[Either[A, B]]
  override type Recovered[A] = Result[Throwable, A]

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

  override def recover[A](value: Future[A])(implicit ec: ExecutionContext): Recovered[A] =
    recover(value, (s: A) => Right(s), { case t => Left(t) })

  //    value.transform({
  //      case Success(s) => Success(Right(s))
  //      case Failure(t) => Success(Left(t))
  //    })

  implicit class FutureEitherInSideOut[A, B](value: Value[A, B])(implicit ec: ExecutionContext) {
    def eitherFuture: Result[A, B] =
      EitherFuture.inSideOut(value)(ec)
  }

  implicit class FutureEitherGetOrFailed[A, B](value: Value[A, B])(implicit ec: ExecutionContext) {
    def eitherFuture: Future[B] =
      EitherFuture.getOrFailed(value)(ec)
  }

  implicit class FutureRecovered[A](value: Future[A])(implicit ec: ExecutionContext) {

    def either: Recovered[A] =
      EitherFuture.recover(value)(ec)

  }

}
