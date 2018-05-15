package pl.writeonly.scalaops.monoid.scalaz

import pl.writeonly.scalaops.monoid.api.future.Ops.{FutureVOps, TransRecover}
import pl.writeonly.scalaops.monoid.api.future.{EC, TypesRight, Utils}
import pl.writeonly.scalaops.monoid.api.present.ToThrowableException.ToThrowable0Exception
import scalaz.Maybe
import scalaz.Maybe.{Empty, Just}

import scala.concurrent.Future

trait MaybeFuture extends TypesRight with Utils {
  override type Value[A] = Maybe[A]

  override def getOrFailed[A](v: FutureV[A])(implicit ec: EC): Future[A] =
    v match {
      case Just(f: Future[A]) => f
      case Empty()            => ToThrowable0Exception() |> Future.failed
    }

  override def inSideOut[A](v: FutureV[A])(implicit ec: EC): ValueF[A] =
    v match {
      case Just(f: Future[A]) => for (a <- f) yield Maybe.fromNullable(a)
      case a: Empty[A]        => Future.successful(a)
    }

  implicit class OptFutureInSideOut[A](v: FutureV[A])
      extends FutureVOps[Value[A], A] {
    override def inSideOut(implicit ec: EC): ValueF[A] =
      MaybeFuture.inSideOut(v)(ec)
    override def getOrFailed(implicit ec: EC): Future[A] =
      MaybeFuture.getOrFailed(v)(ec)
  }

  implicit class OptFutureTransRecover[A](f: Future[A])
      extends TransRecover[A, Value[A]](f) {
    override def transformSuccess: A => Maybe[A] = Maybe.just

    override def recoverFailure: Throwable => Maybe[A] = _ => Maybe.empty
  }

}

object MaybeFuture extends MaybeFuture
