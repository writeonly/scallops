package pl.writeonly.scalaops.api.future

import pl.writeonly.scalaops.ops.FutureOps

import scala.concurrent.Future

object Ops {

  trait GetOrFailed[A] {
    def getOrFailed(implicit ec: EC): Future[A]
  }

  trait InSideOut[A] {
    def inSideOut(implicit ec: EC): Future[A]
  }

  trait FutureVOps[A, B] extends InSideOut[A] with GetOrFailed[B]

  abstract class TransRecover[A, B](f: Future[A]) extends FutureOps {
    def transRecover(implicit ec: EC): Future[B] =
      f.transformAndRecover(transformSuccess, recoverFailure)

    def transToSuccess(implicit ec: EC): Future[B] =
      f.transformToSuccess(transformSuccess, recoverFailure)

    def transformSuccess: A => B

    def recoverFailure: Throwable => B
  }

}
