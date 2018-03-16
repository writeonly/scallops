package pl.writeonly.son2.core.util

import org.scalactic.{Bad, Good, Or}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object FutureSuccesful {

  def opt[A](value: Future[A])(
      implicit ec: ExecutionContext): Future[Option[A]] =
//    value.transform({
//      case Success(s) => Success(Option(s))
//      case Failure(_) => Success(None)
//    })
    transform(value, (s: A) => Option(s), { case _ => None })

  def either[A](value: Future[A])(
      implicit ec: ExecutionContext): Future[Either[Throwable, A]] =
//    value.transform({
//      case Success(s) => Success(Right(s))
//      case Failure(t) => Success(Left(t))
//    })
    transform(value, (s: A) => Right(s), { case t => Left(t) })

  def tryWith[A](value: Future[A])(
      implicit ec: ExecutionContext): Future[Try[A]] =
//    value.transformWith(Future.successful)
    transform(value, (s: A) => Success(s), { case t => Failure(t) })

  def or[A](value: Future[A])(
      implicit ec: ExecutionContext): Future[A Or Throwable] =
//    value.transform({
//      case Success(s) => Success(Good(s))
//      case Failure(t) => Success(Bad(t))
//    })
    transform(value, (s: A) => Good(s), { case t => Bad(t) })

  private def transform[S, T](value: Future[T],
                              s: T => S,
                              pf: PartialFunction[Throwable, S])(
      implicit executor: ExecutionContext): Future[S] =
    value
      .transform(s, e => e)
      .recover(pf)

  implicit class FutureSuccessful[A](value: Future[A])(
      implicit ec: ExecutionContext) {
    def opt: Future[Option[A]] = FutureSuccesful.opt(value)(ec)
    def either: Future[Either[Throwable, A]] = FutureSuccesful.either(value)(ec)
    def tryWith: Future[Try[A]] = FutureSuccesful.tryWith(value)(ec)
    def or: Future[A Or Throwable] = FutureSuccesful.or(value)(ec)
  }

}
