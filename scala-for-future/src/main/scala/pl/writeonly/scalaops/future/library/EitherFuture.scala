package pl.writeonly.scalaops.future.library

import pl.writeonly.scalaops.future.api.Ops.{FutureVOps, TransRecover}
import pl.writeonly.scalaops.future.api.{EC, TypesBoth, Utils}
import pl.writeonly.scalaops.ops.EitherOps

import scala.concurrent.Future

trait EitherFuture extends TypesBoth with Utils with EitherOps {
  override type Value[A, B] = Either[A, B]

  override def getOrFailed[A, B](v: FutureV[A, B])(implicit ec: EC): Future[B] =
    v match {
      case Right(f: Future[B]) => f
      case Left(f: A)          => f |> toThrowable[A] |> Future.failed
    }

  override def inSideOut[A, B](
    v: FutureV[A, B]
  )(implicit ec: EC): ValueF[A, B] =
    v match {
      case Right(f: Future[B]) => for (a <- f) yield Right(a)
      case a: Left[A, B]       => a |> Future.successful
    }

  implicit class EitherFutureVOps[A, B](v: FutureV[A, B])
      extends FutureVOps[Value[A, B], B] {
    override def inSideOut(implicit ec: EC): ValueF[A, B] =
      EitherFuture.inSideOut(v)(ec)
    override def getOrFailed(implicit ec: EC): Future[B] =
      EitherFuture.getOrFailed(v)(ec)
  }

  implicit class EitherFutureTransRecover[A](f: Future[A])
      extends TransRecover[A, Recovered[A]](f) {

    override def transformSuccess = Right.apply

    override def recoverFailure = Left.apply
  }

}

object EitherFuture extends EitherFuture
