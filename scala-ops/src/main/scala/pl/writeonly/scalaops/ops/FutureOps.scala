package pl.writeonly.scalaops.ops

import pl.writeonly.scalaops.pipe.Pipe

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait FutureOps {

  implicit class FutureOps[A](v: Future[A]) extends Pipe {
    def transformAndRecover[B](s: A => B, r: Throwable => B)(
      implicit executor: ExecutionContext
    ): Future[B] =
      v.transform(s, e => e)
        .recover { case t: Throwable => r(t) }

    def transformToSuccess[B](s: A => B, r: Throwable => B)(
      implicit executor: ExecutionContext
    ): Future[B] =
      v.transform({
        case Success(a) => Success(s(a))
        case Failure(t) => Success(r(t))
      })
  }

}

object FutureOps extends FutureOps
