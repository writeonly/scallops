package pl.writeonly.addons.future.scalaz

import pl.writeonly.addons.future.api.Ops.{GetOrFailed, InSideOut, TransRecover}
import pl.writeonly.addons.future.api.{EC, Types2, Utils}
import scalaz.{-\/, \/, \/-}

import scala.concurrent.Future
import scala.concurrent.Future.{failed, successful}

trait HydraFuture extends Types2 with Utils {
  override type Value[A, B] = \/[A, B]

  override def getOrFailed[A, B](
    v: ValueFuture[A, B]
  )(implicit ec: EC): Future[B] =
    v match {
      case \/-(f: Future[B]) => f
      case -\/(f)            => f |> toThrowable |> failed
    }

  override def inSideOut[A, B](
    v: ValueFuture[A, B]
  )(implicit ec: EC): FutureValue[A, B] =
    v match {
      case \/-(f: Future[B]) => for (a <- f) yield \/-(a)
      case a: -\/[A]         => a |> successful
    }

  override def recover[A](v: Future[A])(implicit ec: EC): FutureRecovered[A] =
    v.transformAndRecover((s: A) => \/-(s), { case t => -\/(t) })

  //    value.transform({
  //      case Success(s) => Success(Right(s))
  //      case Failure(t) => Success(Left(t))
  //    })

  implicit class HydraFutureInSideOut[A, B](v: ValueFuture[A, B])
      extends InSideOut[Value[A, B]] {
    override def inSideOut(implicit ec: EC): FutureValue[A, B] =
      HydraFuture.inSideOut(v)(ec)
  }

  implicit class HydraFutureGetOrFailed[A, B](value: ValueFuture[A, B])
      extends GetOrFailed[B] {
    override def getOrFailed(implicit ec: EC): Future[B] =
      HydraFuture.getOrFailed(value)(ec)
  }

  implicit class HydraFutureTransRecover[A](value: Future[A])
      extends TransRecover[Recovered[A]] {

    override def transRecover(implicit ec: EC): FutureRecovered[A] =
      HydraFuture.recover(value)(ec)

  }

}

object HydraFuture extends HydraFuture
