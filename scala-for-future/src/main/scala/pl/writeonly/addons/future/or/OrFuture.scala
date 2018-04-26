package pl.writeonly.addons.future.or

import org.scalactic.Or
import pl.writeonly.addons.future.{Utils, Types2}

import scala.concurrent.{ExecutionContext, Future}

object OrFuture extends Types2 with Utils {

  override type Value[A, B] = Or[Future[B], A]
  override type Result[A, B] = Future[B Or A]
  override type Recovered[A] = Result[A, Throwable]

  override def inSideOut[A, B](value: Value[A, B])(implicit ec: ExecutionContext): Result[A, B] = ???

  override def getOrFailed[A, B](value: Value[A, B])(implicit ec: ExecutionContext): Future[B] = ???

  override def recover[A](value: Future[A])(implicit ec: ExecutionContext): Recovered[A] = ???

  implicit class OrFutureInSideOut[B, A](value: Future[B] Or A)(implicit ec: ExecutionContext) {
    def orFuture: Result[A, B] = OrFuture.inSideOut(value)(ec)
  }

  implicit class OrFutureGetOrFailed[A, B](value: Value[A, B])(implicit ec: ExecutionContext) {
    def orFuture: Future[B] = OrFuture.getOrFailed(value)(ec)
  }

}
