package pl.writeonly.scalaops.ops

import scala.concurrent.{ExecutionContext, Future}

trait FutureOps {
  implicit class FutureOps[A](future: Future[A]) {
    def transformAndRecover[B](s: A => B, pf: PartialFunction[Throwable, B])(
      implicit executor: ExecutionContext
    ): Future[B] =
      future
        .transform(s, e => e)
        .recover(pf)
  }
}

object FutureOps extends FutureOps
