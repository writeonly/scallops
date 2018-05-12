package pl.writeonly.scalaops.future.scalaz

import pl.writeonly.scalaops.future.api.Ops.{
  GetOrFailed,
  InSideOut,
  TransRecover
}
import pl.writeonly.scalaops.future.api.{EC, TypesBoth, Utils}
import scalaz.{-\/, \/, \/-}

import scala.concurrent.Future
import scala.concurrent.Future.{failed, successful}

trait HydraFuture extends TypesBoth with Utils {
  override type Value[A, B] = \/[A, B]

  override def getOrFailed[A, B](v: FutureV[A, B])(implicit ec: EC): Future[B] =
    v match {
      case \/-(f: Future[B]) => f
      case -\/(f: A)         => f |> toThrowable[A] |> failed
    }

  override def inSideOut[A, B](
    v: FutureV[A, B]
  )(implicit ec: EC): ValueF[A, B] =
    v match {
      case \/-(f: Future[B]) => for (a <- f) yield \/-(a)
      case a: -\/[A]         => a |> successful
    }

  implicit class HydraFutureInSideOut[A, B](v: FutureV[A, B])
      extends InSideOut[Value[A, B]] {
    override def inSideOut(implicit ec: EC): ValueF[A, B] =
      HydraFuture.inSideOut(v)(ec)
  }

  implicit class HydraFutureGetOrFailed[A, B](value: FutureV[A, B])
      extends GetOrFailed[B] {
    override def getOrFailed(implicit ec: EC): Future[B] =
      HydraFuture.getOrFailed(value)(ec)
  }

  implicit class HydraFutureTransRecover[A](f: Future[A])
      extends TransRecover[A, Recovered[A]](f) {
    override def transformSuccess: A => Recovered[A] = \/.right

    override def recoverFailure: Throwable => Recovered[A] = \/.left
  }

}

object HydraFuture extends HydraFuture
