package pl.writeonly.scalaops.impl

import pl.writeonly.scalaops.api.future.Ops.{FutureVOps, TransRecover}
import pl.writeonly.scalaops.api.future.{EC, TypesRight, Utils}

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

  implicit class TryFutureVOps[A](value: FutureV[A])
      extends FutureVOps[Value[A], A] {
    override def inSideOut(implicit ec: EC): ValueF[A] =
      TryFuture.inSideOut(value)(ec)
    override def getOrFailed(implicit ec: EC): Future[A] =
      TryFuture.getOrFailed(value)(ec)
  }

  implicit class TryFutureTransRecover[A](f: Future[A])
      extends TransRecover[A, Recovered[A]](f) {

    override def transformSuccess = Success.apply

    override def recoverFailure = Failure.apply
  }

}

object TryFuture extends TryFuture
