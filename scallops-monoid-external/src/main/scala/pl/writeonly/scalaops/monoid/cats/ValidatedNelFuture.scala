package pl.writeonly.scalaops.monoid.cats

import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.implicits._
import pl.writeonly.scalaops.monoid.api.future.Ops.{FutureVOps, TransRecover}
import pl.writeonly.scalaops.monoid.api.future.{EC, TypesBoth, Utils}

import scala.concurrent.Future

trait ValidatedNelFuture extends TypesBoth with Utils {

  override type Value[A, B] = ValidatedNel[A, B]

  override def inSideOut[A, B](
    v: FutureV[A, B]
  )(implicit ec: EC): ValueF[A, B] =
    v match {
      case Valid(f: Future[B])         => for (a <- f) yield Validated.valid(a)
      case a: Invalid[NonEmptyList[A]] => Future.successful(a)
    }

  override def getOrFailed[A, B](v: FutureV[A, B])(implicit ec: EC): Future[B] =
    v match {
      case Valid(f: Future[B]) => f
      case a: Invalid[NonEmptyList[A]] if a.e.size === 1 =>
        a.e.head |> toThrowable[A] |> Future.failed
      case a: Invalid[NonEmptyList[A]] =>
        a.e |> toThrowable[NonEmptyList[A]] |> Future.failed
    }

  implicit class ValidFutureInSideOut[A, B](v: FutureV[A, B])
      extends FutureVOps[Value[A, B], B] {
    override def inSideOut(implicit ec: EC): ValueF[A, B] =
      ValidatedNelFuture.inSideOut(v)(ec)
    override def getOrFailed(implicit ec: EC): Future[B] =
      ValidatedNelFuture.getOrFailed(v)(ec)
  }

  implicit class ValidFutureTransRecover[B](f: Future[B])
      extends TransRecover[B, Recovered[B]](f) {

    override def transformSuccess: B => Recovered[B] = Validated.validNel

    override def recoverFailure: Throwable => Recovered[B] =
      Validated.invalidNel
  }

}

object ValidatedNelFuture extends ValidatedNelFuture
