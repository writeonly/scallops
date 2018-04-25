package pl.writeonly.addons.future.`with`

import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future.failed
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object TryFutureGetOrFailed {

  def tryFuture[A](
    value: Try[Future[A]]
  )(implicit ec: ExecutionContext): Future[A] = value match {
    case Success(f: Future[A]) => f
    case Failure(f: Throwable) => f |> failed
  }

  implicit class FutureTry[A](value: Try[Future[A]])(
    implicit ec: ExecutionContext
  ) {
    def tryFuture: Future[A] = TryFutureGetOrFailed.tryFuture(value)(ec)
  }

}
