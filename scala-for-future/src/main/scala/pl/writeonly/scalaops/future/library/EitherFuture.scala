package pl.writeonly.scalaops.future.library

import pl.writeonly.scalaops.future.api.Ops.{
  GetOrFailed,
  InSideOut,
  TransRecover
}
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

  override def transRecover[A](v: Future[A])(implicit ec: EC): RecoveredF[A] =
    v.transformAndRecover((s: A) => Right(s), { case t => Left(t) })

  //    value.transform({
  //      case Success(s) => Success(Right(s))
  //      case Failure(t) => Success(Left(t))
  //    })

  implicit class EitherFutureInSideOut[A, B](v: FutureV[A, B])
      extends InSideOut[Value[A, B]] {
    override def inSideOut(implicit ec: EC): ValueF[A, B] =
      EitherFuture.inSideOut(v)(ec)
  }

  implicit class EitherFutureGetOrFailed[A, B](value: FutureV[A, B])
      extends GetOrFailed[B] {
    override def getOrFailed(implicit ec: EC): Future[B] =
      EitherFuture.getOrFailed(value)(ec)
  }

  implicit class EitherFutureTransRecover[A](value: Future[A])
      extends TransRecover[Recovered[A]] {

    override def transRecover(implicit ec: EC): RecoveredF[A] =
      EitherFuture.transRecover(value)(ec)

  }

}

object EitherFuture extends EitherFuture
