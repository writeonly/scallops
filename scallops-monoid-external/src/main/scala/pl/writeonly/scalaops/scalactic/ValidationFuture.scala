package pl.writeonly.scalaops.scalactic

import org.scalactic.{Fail, Pass, Validation}
import pl.writeonly.scalaops.monoid.api.future.Ops.{InSideOut, TransRecover}
import pl.writeonly.scalaops.monoid.api.future.{EC, TypesLeft, Utils}

import scala.concurrent.Future

trait ValidationFuture extends TypesLeft with Utils {

  override type Value[A] = Validation[A]

  override def unitOrFailed[A](v: FutureV[A])(implicit ec: EC): Future[Unit] =
    v match {
      case Pass    => Future.unit
      case Fail(f) => f |> toThrowable[A] |> Future.failed
    }

  override def neverOrFailed[A](
    v: FutureV[A]
  )(implicit ec: EC): Future[Nothing] =
    v match {
      case Pass    => Future.never
      case Fail(f) => f |> toThrowable[A] |> Future.failed
    }

  override def getOrFailed[A, B](v: FutureV[A],
                                 b: B)(implicit ec: EC): Future[B] =
    v match {
      case Pass    => Future.successful(b)
      case Fail(f) => f |> toThrowable[A] |> Future.failed
    }

  override def inSideOut[A](v: FutureV[A])(implicit ec: EC): ValueF[A] =
    v match {
      case Pass        => Future.successful(null)
      case a @ Fail(_) => a |> Future.successful
    }

  implicit class OrFutureInSideOut[A](v: FutureV[A])
      extends InSideOut[Value[A]] {
    override def inSideOut(implicit ec: EC): ValueF[A] =
      ValidationFuture.inSideOut(v)(ec)

    def unitOrFailed[A](implicit ec: EC): Future[Unit] =
      ValidationFuture.unitOrFailed(v)(ec)

    def neverOrFailed[A](implicit ec: EC): Future[Nothing] =
      ValidationFuture.neverOrFailed(v)(ec)

    def getOrFailed[B](b: B)(implicit ec: EC): Future[B] =
      ValidationFuture.getOrFailed(v, b)(ec)
  }

  implicit class ValidationFutureTransRecover[A](f: Future[A])
      extends TransRecover[A, Recovered](f) {
    override def transformSuccess: A => Recovered = _ => Pass

    override def recoverFailure: Throwable => Recovered = Fail.apply
  }
}

object ValidationFuture extends ValidationFuture
