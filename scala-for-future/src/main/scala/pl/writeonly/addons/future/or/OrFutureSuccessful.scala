package pl.writeonly.addons.future.or

import org.scalactic.{Bad, Good, Or}
import pl.writeonly.addons.future.Utils

import scala.concurrent.{ExecutionContext, Future}

object OrFutureSuccessful extends Utils {

  def or[A](value: Future[A])(implicit ec: ExecutionContext): Future[A Or Throwable] =
    recover(value, (s: A) => Good(s), { case t => Bad(t) })
  //    value.transform({
  //      case Success(s) => Success(Good(s))
  //      case Failure(t) => Success(Bad(t))
  //    })

  implicit class FutureSuccessful[A](value: Future[A])(implicit ec: ExecutionContext) {

    def or: Future[A Or Throwable] = OrFutureSuccessful.or(value)(ec)
  }

}
