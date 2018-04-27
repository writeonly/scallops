package pl.writeonly.addons.future.ops

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import pl.writeonly.addons.future.api.Ops.{GetOrFailed, InSideOut, Recover}
import pl.writeonly.addons.future.api.{EC, Types2, Utils}
import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future
import scala.concurrent.Future.{failed, successful}

object ValidFuture extends Types2 with Utils {

  override type Value[A, B] = Validated[A, B]

  override def inSideOut[A, B](
    v: ValueFuture[A, B]
  )(implicit ec: EC): FutureValue[A, B] =
    v match {
      case Valid(f: Future[B]) => for (a <- f) yield Valid(a)
      case a: Invalid[A]       => a |> successful
    }

  override def getOrFailed[A, B](
    v: ValueFuture[A, B]
  )(implicit ec: EC): Future[B] =
    v match {
      case Valid(f: Future[B]) => f
      case a: Invalid[A]       => a |> toThrowable |> failed
    }

  override def recover[B](v: Future[B])(implicit ec: EC): FutureRecovered[B] =
    transformAndRecover(v, (s: B) => Valid(s), { case t => Invalid(t) })

  //    value.transform({
  //      case Success(s) => Success(Good(s))
  //      case Failure(t) => Success(Bad(t))
  //    })

  implicit class ValidFutureInSideOut[A, B](v: ValueFuture[A, B])
      extends InSideOut[Value[A, B]] {
    override def inSideOut(implicit ec: EC): FutureValue[A, B] =
      ValidFuture.inSideOut(v)(ec)
  }

  implicit class ValidFutureGetOrFailed[A, B](v: ValueFuture[A, B])
      extends GetOrFailed[B] {
    override def getOrFailed(implicit ec: EC): Future[B] =
      ValidFuture.getOrFailed(v)(ec)
  }

  implicit class ValidFutureRecover[B](v: Future[B])
      extends Recover[Recovered[B]] {
    override def recover(implicit ec: EC): FutureRecovered[B] =
      ValidFuture.recover(v)(ec)
  }

}
