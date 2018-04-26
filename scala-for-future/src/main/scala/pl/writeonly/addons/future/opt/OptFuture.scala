package pl.writeonly.addons.future.opt

import pl.writeonly.addons.future.{Utils, Types1}
import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future.{failed, successful}
import scala.concurrent.{ExecutionContext, Future}

object OptFuture extends Types1 with Utils {

  override type Value[A] = Option[Future[A]]
  override type Result[A] = Future[Option[A]]

  override type Recovered[A] = Result[A]

  override def getOrFailed[A](value: Value[A])(implicit ec: ExecutionContext): Future[A] = value match {
    case Some(f: Future[A]) => f
    case None               => new IllegalStateException() |> failed
  }

  override def inSideOut[A](value: Value[A])(implicit ec: ExecutionContext): Result[A] = value match {
    case Some(f: Future[A]) => for (a <- f) yield Option(a)
    case None               => None |> successful
  }

  override def recover[A](value: Future[A])(implicit ec: ExecutionContext): Recovered[A] =
    recover(value, (s: A) => Option(s), { case _ => None })

  //    value.transform({
  //      case Success(s) => Success(Option(s))
  //      case Failure(_) => Success(None)
  //    })

  implicit class OptFutureGetOrFailed[A](value: Value[A])(implicit ec: ExecutionContext) {
    def optFuture: Future[A] = OptFuture.getOrFailed(value)(ec)
  }

  implicit class OptFutureInSideOut[A](value: Value[A])(implicit ec: ExecutionContext) {
    def optFuture: Result[A] = OptFuture.inSideOut(value)(ec)
  }

  implicit class FutureRecovered[A](value: Future[A])(implicit ec: ExecutionContext) {
    def opt: Recovered[A] = OptFuture.recover(value)(ec)
  }

}
