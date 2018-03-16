package pl.writeonly.son2.core.util

import org.scalactic.{Bad, Good, Or}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object FutureGetOrFailed {
  def optFuture[A](value: Option[Future[A]])(
      implicit ec: ExecutionContext): Future[A] = value match {
    case Some(f: Future[A]) => f
    case None               => Future.failed(new IllegalStateException())
  }

  def eitherFuture[A, B](value: Either[A, Future[B]])(
      implicit ec: ExecutionContext): Future[B] =
    value match {
      case Right(f: Future[B]) => f
      case Left(f: A)          => Future.failed(toThrowable(f))
    }

  def tryFuture[A](value: Try[Future[A]])(
      implicit ec: ExecutionContext): Future[A] = value match {
    case Success(f: Future[A]) => f
    case Failure(f: Throwable) => Future.failed(f)
  }

  def orFuture[A, B](value: Future[A] Or B)(
      implicit ec: ExecutionContext): Future[A] = value match {
    case Good(f: Future[A]) => f
    case Bad(f: B)          => Future.failed(toThrowable(f))
  }

  private def toThrowable(a: Any): Throwable = a match {
    case f: Throwable => f
    case _            => new IllegalStateException("" + a)
  }

  private def identity[A](a: A) = a

  implicit class FutureOpt[A](opt: Option[Future[A]])(
      implicit ec: ExecutionContext) {
    def optFuture: Future[A] = FutureGetOrFailed.optFuture(opt)(ec)
  }

  implicit class FutureEither[A, B](value: Either[A, Future[B]])(
      implicit ec: ExecutionContext) {
    def eitherFuture: Future[B] = FutureGetOrFailed.eitherFuture(value)(ec)
  }

  implicit class FutureTry[A](value: Try[Future[A]])(
      implicit ec: ExecutionContext) {
    def tryFuture: Future[A] = FutureGetOrFailed.tryFuture(value)(ec)
  }

  implicit class FutureOr[A, B](value: Future[A] Or B)(
      implicit ec: ExecutionContext) {
    def orFuture: Future[A] = FutureGetOrFailed.orFuture(value)(ec)
  }

}
