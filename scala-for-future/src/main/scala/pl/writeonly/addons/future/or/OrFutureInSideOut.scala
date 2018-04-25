package pl.writeonly.addons.future.or

import org.scalactic.{Bad, Good, Or}
import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future.successful
import scala.concurrent.{ExecutionContext, Future}

object OrFutureInSideOut {

  def orFuture[A, B](
    value: Future[A] Or B
  )(implicit ec: ExecutionContext): Future[A Or B] = value match {
    case Good(f: Future[A]) => for (a <- f) yield Good(a)
    case Bad(a)             => Bad(a) |> successful
  }

  implicit class FutureOr[A, B](value: Future[A] Or B)(
    implicit ec: ExecutionContext
  ) {
    def orFuture: Future[A Or B] = OrFutureInSideOut.orFuture(value)(ec)
  }

}
