package pl.writeonly.addons.future.`with`

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object TryFutureSuccessful {

  def tryWith[A](value: Future[A])(implicit ec: ExecutionContext): Future[Try[A]] =
  //    value.transformWith(Future.successful)
    transform(value, (s: A) => Success(s), { case t => Failure(t) })

  private def transform[S, T](value: Future[T], s: T => S, pf: PartialFunction[Throwable, S])(
    implicit executor: ExecutionContext
  ): Future[S] =
    value
      .transform(s, e => e)
      .recover(pf)

  implicit class FutureSuccessful[A](value: Future[A])(implicit ec: ExecutionContext) {

    def tryWith: Future[Try[A]] = TryFutureSuccessful.tryWith(value)(ec)

  }

}
