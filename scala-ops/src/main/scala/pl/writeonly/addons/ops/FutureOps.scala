package pl.writeonly.addons.ops

import scala.concurrent.{ExecutionContext, Future}

object FutureOps {
  implicit class FutureOps[A](future: Future[A]) {
    def transformAndRecover[B](s: A => B, pf: PartialFunction[Throwable, B])(
      implicit executor: ExecutionContext
    ): Future[B] =
      future
        .transform(s, e => e)
        .recover(pf)
  }
}
