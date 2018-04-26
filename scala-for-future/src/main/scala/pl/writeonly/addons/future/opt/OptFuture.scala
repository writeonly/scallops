package pl.writeonly.addons.future.opt

import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future.{failed, successful}
import scala.concurrent.{ExecutionContext, Future}

object OptFuture extends OptTypes1 {
  override def getOrFailed[A](value: Value[A])(implicit ec: ExecutionContext): Future[A] = value match {
    case Some(f: Future[A]) => f
    case None => new IllegalStateException() |> failed
  }

  override def inSideOut[A](value: Value[A])(implicit ec: ExecutionContext): Result[A] = value match {
    case Some(f: Future[A]) => for (a <- f) yield Option(a)
    case None => None |> successful
  }

  implicit class OptFutureGetOrFailed[A](value: Value[A])(implicit ec: ExecutionContext) {
    def optFuture: Future[A] = OptFuture.getOrFailed(value)(ec)
  }

  implicit class OptFutureInSideOut[A](value: Value[A])(implicit ec: ExecutionContext) {
    def optFuture: Result[A] = OptFuture.inSideOut(value)(ec)
  }

}
