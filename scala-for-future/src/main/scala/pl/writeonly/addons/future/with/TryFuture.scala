package pl.writeonly.addons.future.`with`

import pl.writeonly.addons.future.{Utils, Types1}
import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future.{failed, successful}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object TryFuture extends Types1 with Utils {

  override type Value[A] = Try[Future[A]]
  override type Result[A] = Future[Try[A]]
  override type Recovered[A] = Result[A]

  def getOrFailed[A](value: Value[A])(implicit ec: ExecutionContext): Future[A] = value match {
    case Success(f: Future[A]) => f
    case Failure(f: Throwable) => f |> failed
  }

  def inSideOut[A](value: Value[A])(implicit ec: ExecutionContext): Result[A] = value match {
    case Success(f: Future[A]) => for (a <- f) yield Success(a)
    case a: Failure[A]         => a |> successful
  }

  override def recover[A](value: Future[A])(implicit ec: ExecutionContext): Recovered[A] =
    recover(value, (s: A) => Success(s), { case t => Failure(t) })

  //    value.transformWith(Future.successful)

  implicit class TryFutureGetOrFailed[A](value: Value[A])(implicit ec: ExecutionContext) {
    def tryFuture: Future[A] = TryFuture.getOrFailed(value)(ec)
  }

  implicit class TryFutureInSideOut[A](value: Value[A])(implicit ec: ExecutionContext) {
    def tryFuture: Result[A] = TryFuture.inSideOut(value)(ec)
  }

  implicit class FutureRecovered[A](value: Future[A])(implicit ec: ExecutionContext) {

    def tryWith: Recovered[A] = TryFuture.recover(value)(ec)

  }

}
