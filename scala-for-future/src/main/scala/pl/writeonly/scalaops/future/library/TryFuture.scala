package pl.writeonly.scalaops.future.library

import pl.writeonly.scalaops.future.api.Ops.{GetOrFailed, InSideOut, TransRecover}
import pl.writeonly.scalaops.future.api.{EC, TypesRight, Utils}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait TryFuture extends TypesRight with Utils {

  override type Value[A] = Try[A]

  def getOrFailed[A](v: FutureV[A])(implicit ec: EC): Future[A] =
    v match {
      case Success(f: Future[A]) => f
      case Failure(f: Throwable) => f |> Future.failed
    }

  def inSideOut[A](v: FutureV[A])(implicit ec: EC): ValueF[A] =
    v match {
      case Success(f: Future[A]) => for (a <- f) yield Success(a)
      case a: Failure[A]         => a |> Future.successful
    }

  override def transRecover[A](v: Future[A])(implicit ec: EC): RecoveredF[A] =
    v.transformAndRecover((s: A) => Success(s), { case t => Failure(t) })

  implicit class TryFutureGetOrFailed[A](value: FutureV[A])
      extends GetOrFailed[A] {
    override def getOrFailed(implicit ec: EC): Future[A] =
      TryFuture.getOrFailed(value)(ec)
  }

  implicit class TryFutureInSideOut[A](value: FutureV[A])
      extends InSideOut[Value[A]] {
    override def inSideOut(implicit ec: EC): ValueF[A] =
      TryFuture.inSideOut(value)(ec)
  }

  implicit class TryFutureTransRecover[A](value: Future[A])
      extends TransRecover[Recovered[A]] {

    override def transRecover(implicit ec: EC): RecoveredF[A] =
      TryFuture.transRecover(value)(ec)

  }

}

object TryFuture extends TryFuture
