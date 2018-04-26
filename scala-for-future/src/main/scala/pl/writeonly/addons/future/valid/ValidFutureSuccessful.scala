package pl.writeonly.addons.future.validated

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}

import scala.concurrent.{ExecutionContext, Future}

object ValidFutureSuccessful {

  private def transform[S, T](value: Future[T], s: T => S, pf: PartialFunction[Throwable, S])(
    implicit executor: ExecutionContext
  ): Future[S] =
    value
      .transform(s, e => e)
      .recover(pf)

  def validated[A](value: Future[A])(implicit ec: ExecutionContext): Future[Validated[Throwable, A]] =
    //    value.transform({
    //      case Success(s) => Success(Right(s))
    //      case Failure(t) => Success(Left(t))
    //    })
    transform(value, (s: A) => Valid(s), { case t => Invalid(t) })

  implicit class FutureSuccessful[A](value: Future[A])(implicit ec: ExecutionContext) {

    def either: Future[Validated[Throwable, A]] =
      ValidFutureSuccessful.validated(value)(ec)

  }

}
