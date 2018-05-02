package pl.writeonly.addons.future.scalaz

import pl.writeonly.addons.future.api.Ops.{GetOrFailed, InSideOut, TransRecover}
import pl.writeonly.addons.future.api.{EC, Types1, Utils}
import scalaz.Maybe
import scalaz.Maybe.{Empty, Just}

import scala.concurrent.Future

trait MaybeFuture extends Types1 with Utils {
  override type Value[A] = Maybe[A]

  override def getOrFailed[A](v: ValueFuture[A])(implicit ec: EC): Future[A] =
    v match {
      case Just(f: Future[A]) => f
      case Empty()            => new IllegalStateException() |> Future.failed
    }

  override def inSideOut[A](
    v: ValueFuture[A]
  )(implicit ec: EC): FutureValue[A] =
    v match {
      case Just(f: Future[A]) => for (a <- f) yield Maybe.fromNullable(a)
      case a: Empty[A]        => Future.successful(a)
    }

  override def transRecover[A](
    v: Future[A]
  )(implicit ec: EC): FutureRecovered[A] =
    v.transformAndRecover((s: A) => Just(s), { case _ => Maybe.empty })

  //    value.transform({
  //      case Success(s) => Success(Option(s))
  //      case Failure(_) => Success(Empty())
  //    })

  implicit class OptFutureGetOrFailed[A](v: ValueFuture[A])
      extends GetOrFailed[A] {
    override def getOrFailed(implicit ec: EC): Future[A] =
      MaybeFuture.getOrFailed(v)(ec)
  }

  implicit class OptFutureInSideOut[A](v: ValueFuture[A])
      extends InSideOut[Value[A]] {
    override def inSideOut(implicit ec: EC): FutureValue[A] =
      MaybeFuture.inSideOut(v)(ec)
  }

  implicit class OptFutureTransRecover[A](v: Future[A])
      extends TransRecover[Value[A]] {
    override def transRecover(implicit ec: EC): FutureRecovered[A] =
      MaybeFuture.transRecover(v)(ec)
  }
}

object MaybeFuture extends MaybeFuture
