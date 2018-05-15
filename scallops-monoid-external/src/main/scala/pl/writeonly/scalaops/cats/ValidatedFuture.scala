package pl.writeonly.scalaops.cats

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import pl.writeonly.scalaops.monoid.api.future.Ops.{FutureVOps, TransRecover}
import pl.writeonly.scalaops.monoid.api.future.{EC, TypesBoth, Utils}

import scala.concurrent.Future

trait ValidatedFuture extends TypesBoth with Utils {

  override type Value[A, B] = Validated[A, B]

  override def inSideOut[A, B](
    v: FutureV[A, B]
  )(implicit ec: EC): ValueF[A, B] =
    v match {
      case Valid(f: Future[B]) => for (a <- f) yield Validated.valid(a)
      case a: Invalid[A]       => a |> Future.successful
    }

  override def getOrFailed[A, B](v: FutureV[A, B])(implicit ec: EC): Future[B] =
    v match {
      case Valid(f: Future[B]) => f
      case a: Invalid[A]       => a |> toThrowable[Invalid[A]] |> Future.failed
    }

  implicit class ValidFutureInSideOut[A, B](v: FutureV[A, B])
      extends FutureVOps[Value[A, B], B] {
    override def inSideOut(implicit ec: EC): ValueF[A, B] =
      ValidatedFuture.inSideOut(v)(ec)
    override def getOrFailed(implicit ec: EC): Future[B] =
      ValidatedFuture.getOrFailed(v)(ec)
  }

  implicit class ValidFutureTransRecover[B](f: Future[B])
      extends TransRecover[B, Recovered[B]](f) {
    override def transformSuccess: B => Recovered[B] = Validated.valid

    override def recoverFailure: Throwable => Recovered[B] = Validated.invalid
  }

}

object ValidatedFuture extends ValidatedFuture
