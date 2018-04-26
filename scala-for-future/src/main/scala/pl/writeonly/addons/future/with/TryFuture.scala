package pl.writeonly.addons.future.`with`

import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future.{failed, successful}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object TryFuture extends TryTypes1 {

  def getOrFailed[A](value: Value[A])(implicit ec: ExecutionContext): Future[A] = value match {
    case Success(f: Future[A]) => f
    case Failure(f: Throwable) => f |> failed
  }

  def inSideOut[A](value: Value[A])(implicit ec: ExecutionContext): Result[A] = value match {
    case Success(f: Future[A]) => for (a <- f) yield Success(a)
    case Failure(a) => Failure(a) |> successful
  }

  implicit class TryFutureGetOrFailed[A](value: Value[A])(implicit ec: ExecutionContext) {
    def tryFuture: Future[A] = TryFuture.getOrFailed(value)(ec)
  }

  implicit class TryFutureInSideOut[A](value: Value[A])(implicit ec: ExecutionContext) {
    def tryFuture: Result[A] = TryFuture.inSideOut(value)(ec)
  }

}
