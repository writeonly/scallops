package pl.writeonly.addons.future.ops

import pl.writeonly.addons.future.api.Ops.{GetOrFailed, InSideOut, Recover}
import pl.writeonly.addons.future.api.{EC, Types2, Utils}
import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future
import scala.concurrent.Future.{failed, successful}

object EitherFuture extends Types2 with Utils {
  override type Value[A, B] = Either[A, B]

  override def getOrFailed[A, B](
    v: ValueFuture[A, B]
  )(implicit ec: EC): Future[B] =
    v match {
      case Right(f: Future[B]) => f
      case Left(f)             => f |> toThrowable |> failed
    }

  override def inSideOut[A, B](
    v: ValueFuture[A, B]
  )(implicit ec: EC): FutureValue[A, B] =
    v match {
      case Right(f: Future[B]) => for (a <- f) yield Right(a)
      case a: Left[A, B]       => a |> successful
    }

  override def recover[A](v: Future[A])(implicit ec: EC): FutureRecovered[A] =
    transformAndRecover(v, (s: A) => Right(s), { case t => Left(t) })

  //    value.transform({
  //      case Success(s) => Success(Right(s))
  //      case Failure(t) => Success(Left(t))
  //    })

  implicit class EitherFutureInSideOut[A, B](v: ValueFuture[A, B])
      extends InSideOut[Value[A, B]] {
    override def inSideOut(implicit ec: EC): FutureValue[A, B] =
      EitherFuture.inSideOut(v)(ec)
  }

  implicit class EitherFutureGetOrFailed[A, B](value: ValueFuture[A, B])
      extends GetOrFailed[B] {
    override def getOrFailed(implicit ec: EC): Future[B] =
      EitherFuture.getOrFailed(value)(ec)
  }

  implicit class EitherFutureRecover[A](value: Future[A])
      extends Recover[Recovered[A]] {

    override def recover(implicit ec: EC): FutureRecovered[A] =
      EitherFuture.recover(value)(ec)

  }

}
