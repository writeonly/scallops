package pl.writeonly.addons.future.or

import org.scalactic.{Bad, Good, Or}

import scala.concurrent.{ExecutionContext, Future}

object OrFutureSuccesful {

  private def transform[S, T](
    value: Future[T],
    s: T => S,
    pf: PartialFunction[Throwable, S]
  )(implicit executor: ExecutionContext): Future[S] =
    value
      .transform(s, e => e)
      .recover(pf)

  def or[A](
    value: Future[A]
  )(implicit ec: ExecutionContext): Future[A Or Throwable] =
    //    value.transform({
    //      case Success(s) => Success(Good(s))
    //      case Failure(t) => Success(Bad(t))
    //    })
    transform(value, (s: A) => Good(s), { case t => Bad(t) })

  implicit class FutureSuccessful[A](value: Future[A])(
    implicit ec: ExecutionContext
  ) {

    def or: Future[A Or Throwable] = OrFutureSuccesful.or(value)(ec)
  }

}
