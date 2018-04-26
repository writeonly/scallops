package pl.writeonly.addons.future.opt

import org.scalactic.{Bad, Good, Or}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object OptFutureSuccessful {

  def opt[A](value: Future[A])(implicit ec: ExecutionContext): Future[Option[A]] =
    //    value.transform({
    //      case Success(s) => Success(Option(s))
    //      case Failure(_) => Success(None)
    //    })
    transform(value, (s: A) => Option(s), { case _ => None })

  private def transform[S, T](value: Future[T], s: T => S, pf: PartialFunction[Throwable, S])(
    implicit executor: ExecutionContext
  ): Future[S] =
    value
      .transform(s, e => e)
      .recover(pf)

  implicit class FutureSuccessful[A](value: Future[A])(implicit ec: ExecutionContext) {
    def opt: Future[Option[A]] = OptFutureSuccessful.opt(value)(ec)
  }

}
