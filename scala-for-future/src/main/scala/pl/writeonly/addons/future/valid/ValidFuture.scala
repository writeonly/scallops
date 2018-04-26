package pl.writeonly.addons.future.valid

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import pl.writeonly.addons.future.{Utils, Types2}
import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future.{failed, successful}
import scala.concurrent.{ExecutionContext, Future}

object ValidFuture extends Types2 with Utils {

  type Value[A, B] = Validated[A, Future[B]]
  type Result[A, B] = Future[Validated[A, B]]
  type Recovered[A] = Result[Throwable, A]

  override def inSideOut[A, B](value: Value[A, B])(implicit ec: ExecutionContext): Result[A, B] = value match {
    case Valid(f: Future[B]) => for (a <- f) yield Valid(a)
    case Invalid(a)          => Invalid(a) |> successful
  }

  override def getOrFailed[A, B](value: Value[A, B])(implicit ec: ExecutionContext): Future[B] =
    value match {
      case Valid(f: Future[B]) => f
      case Invalid(f)          => f |> toThrowable |> failed
    }

  override def recover[A](value: Future[A])(implicit ec: ExecutionContext): Recovered[A] =
    recover(value, (s: A) => Valid(s), { case t => Invalid(t) })

  //    value.transform({
  //      case Success(s) => Success(Right(s))
  //      case Failure(t) => Success(Left(t))
  //    })

  implicit class ValidFutureInSideOut[A, B](value: Value[A, B])(implicit ec: ExecutionContext) {
    def eitherFuture: Result[A, B] =
      ValidFuture.inSideOut(value)(ec)
  }

  implicit class ValidFutureGetOrFailed[A, B](value: Value[A, B])(implicit ec: ExecutionContext) {
    def eitherFuture: Future[B] = ValidFuture.getOrFailed(value)(ec)
  }

  implicit class ValidFutureRecover[A](value: Future[A])(implicit ec: ExecutionContext) {
    def eitherFuture: Future[Validated[Throwable, A]] =
      ValidFuture.recover(value)(ec)
  }

}
