package pl.writeonly.scalaops.scalaz

import pl.writeonly.scalaops.api.future.Ops.{FutureVOps, TransRecover}
import pl.writeonly.scalaops.api.future.{EC, TypesBoth, Utils}
import scalaz.{Failure, NonEmptyList, Success, Validation, ValidationNel}

import scala.concurrent.Future

trait ValidationNelFuture extends TypesBoth with Utils {

  override type Value[A, B] = ValidationNel[A, B]

  override def inSideOut[A, B](
    v: FutureV[A, B]
  )(implicit ec: EC): ValueF[A, B] =
    v match {
      case Success(f: Future[B])       => for (a <- f) yield Validation.success(a)
      case a: Failure[NonEmptyList[A]] => Future.successful(a)
    }

  override def getOrFailed[A, B](v: FutureV[A, B])(implicit ec: EC): Future[B] =
    v match {
      case Success(f: Future[B]) => f
      case a: Failure[NonEmptyList[A]] if a.e.size == 1 =>
        a.e.head |> toThrowable[A] |> Future.failed
      case a: Failure[NonEmptyList[A]] =>
        a.e |> toThrowable[NonEmptyList[A]] |> Future.failed
    }

  implicit class SuccessFutureInSideOut[A, B](v: FutureV[A, B])
      extends FutureVOps[Value[A, B], B] {
    override def inSideOut(implicit ec: EC): ValueF[A, B] =
      ValidationNelFuture.inSideOut(v)(ec)
    override def getOrFailed(implicit ec: EC): Future[B] =
      ValidationNelFuture.getOrFailed(v)(ec)
  }

  implicit class SuccessFutureTransRecover[B](f: Future[B])
      extends TransRecover[B, Recovered[B]](f) {
    override def transformSuccess: B => Recovered[B] = Validation.success

    override def recoverFailure: Throwable => Recovered[B] =
      Validation.failureNel
  }

}

object ValidationNelFuture extends ValidationNelFuture
