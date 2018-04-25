package pl.writeonly.addons.future.`with`

import scala.concurrent.Future.successful
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

import pl.writeonly.addons.pipe.Pipe._

object TryFutureInSideOut {

  def tryFuture[A](
    value: Try[Future[A]]
  )(implicit ec: ExecutionContext): Future[Try[A]] = value match {
    case Success(f: Future[A]) => for (a <- f) yield Success(a)
    case Failure(a)            => Failure(a) |> successful
  }

  implicit class FutureTry[A](value: Try[Future[A]])(
    implicit ec: ExecutionContext
  ) {
    def tryFuture: Future[Try[A]] = TryFutureInSideOut.tryFuture(value)(ec)
  }

}
