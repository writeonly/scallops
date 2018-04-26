package pl.writeonly.addons.future.or

import org.scalactic.Or
import pl.writeonly.addons.future.ToThrowable

import scala.concurrent.{ExecutionContext, Future}

object OrFuture extends OrTypes2 with ToThrowable {
  override def inSideOut[A, B](value: Value[A, B])(implicit ec: ExecutionContext): Result[A, B] = ???

  override def getOrFailed[A, B](value: Value[A, B])(implicit ec: ExecutionContext): Future[B] = ???

  implicit class OrFutureInSideOut[B, A](value: Future[B] Or A)(implicit ec: ExecutionContext) {
    def orFuture: Result[A, B] = OrFuture.inSideOut(value)(ec)
  }

  implicit class OrFutureGetOrFailed[A, B](value: Value[A, B])(implicit ec: ExecutionContext) {
    def orFuture: Future[B] = OrFuture.getOrFailed(value)(ec)
  }

}
