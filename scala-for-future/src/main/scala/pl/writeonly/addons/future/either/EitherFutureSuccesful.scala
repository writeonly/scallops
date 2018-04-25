package pl.writeonly.addons.future.either

import scala.concurrent.{ExecutionContext, Future}

object EitherFutureSuccesful {

  private def transform[S, T](
    value: Future[T],
    s: T => S,
    pf: PartialFunction[Throwable, S]
  )(implicit executor: ExecutionContext): Future[S] =
    value
      .transform(s, e => e)
      .recover(pf)

  def either[A](
    value: Future[A]
  )(implicit ec: ExecutionContext): Future[Either[Throwable, A]] =
    //    value.transform({
    //      case Success(s) => Success(Right(s))
    //      case Failure(t) => Success(Left(t))
    //    })
    transform(value, (s: A) => Right(s), { case t => Left(t) })

  implicit class FutureSuccessful[A](value: Future[A])(
    implicit ec: ExecutionContext
  ) {

    def either: Future[Either[Throwable, A]] =
      EitherFutureSuccesful.either(value)(ec)

  }

}
