package pl.writeonly.scalaops.monoid.scalaz

import pl.writeonly.scalaops.monoid.api.future.Ops.{FutureVOps, TransRecover}
import pl.writeonly.scalaops.monoid.api.future.{EC, TypesBoth, Utils}
import scalaz.{Failure, Success, Validation}

import scala.concurrent.Future

trait ValidationFuture extends TypesBoth with Utils {

  override type Value[A, B] = Validation[A, B]

  override def inSideOut[A, B](
    v: FutureV[A, B]
  )(implicit ec: EC): ValueF[A, B] =
    v match {
      case Success(f: Future[B]) => for (a <- f) yield Validation.success(a)
      case a: Failure[A]         => a |> Future.successful
    }

  override def getOrFailed[A, B](v: FutureV[A, B])(implicit ec: EC): Future[B] =
    v match {
      case Success(f: Future[B]) => f
      case a: Failure[A]         => a |> toThrowable[Failure[A]] |> Future.failed
    }

  implicit class SuccessFutureInSideOut[A, B](v: FutureV[A, B])
      extends FutureVOps[Value[A, B], B] {
    override def inSideOut(implicit ec: EC): ValueF[A, B] =
      ValidationFuture.inSideOut(v)(ec)
    override def getOrFailed(implicit ec: EC): Future[B] =
      ValidationFuture.getOrFailed(v)(ec)
  }

  implicit class SuccessFutureTransRecover[B](f: Future[B])
      extends TransRecover[B, Recovered[B]](f) {

    override def transformSuccess: B => Recovered[B] = Validation.success

    override def recoverFailure: Throwable => Recovered[B] = Validation.failure
  }

}

object ValidationFuture extends ValidationFuture
