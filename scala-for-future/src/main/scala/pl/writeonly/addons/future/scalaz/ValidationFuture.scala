package pl.writeonly.addons.future.scalaz

import pl.writeonly.addons.future.api.Ops.{GetOrFailed, InSideOut, TransRecover}
import pl.writeonly.addons.future.api.{EC, Types2, Utils}
import scalaz.{Failure, Success, Validation}

import scala.concurrent.Future

object ValidationFuture extends Types2 with Utils {

  override type Value[A, B] = Validation[A, B]

  override def inSideOut[A, B](
    v: ValueFuture[A, B]
  )(implicit ec: EC): FutureValue[A, B] =
    v match {
      case Success(f: Future[B]) => for (a <- f) yield Validation.success(a)
      case a: Failure[A]         => a |> Future.successful
    }

  override def getOrFailed[A, B](
    v: ValueFuture[A, B]
  )(implicit ec: EC): Future[B] =
    v match {
      case Success(f: Future[B]) => f
      case a: Failure[A]         => a |> toThrowable |> Future.failed
    }

  override def recover[B](v: Future[B])(implicit ec: EC): FutureRecovered[B] =
    v.transformAndRecover((s: B) => Success(s), {
      case t => Validation.failure(t)
    })

  //    value.transform({
  //      case Success(s) => Success(Good(s))
  //      case Failure(t) => Success(Bad(t))
  //    })

  implicit class SuccessFutureInSideOut[A, B](v: ValueFuture[A, B])
      extends InSideOut[Value[A, B]] {
    override def inSideOut(implicit ec: EC): FutureValue[A, B] =
      ValidationFuture.inSideOut(v)(ec)
  }

  implicit class SuccessFutureGetOrFailed[A, B](v: ValueFuture[A, B])
      extends GetOrFailed[B] {
    override def getOrFailed(implicit ec: EC): Future[B] =
      ValidationFuture.getOrFailed(v)(ec)
  }

  implicit class SuccessFutureTransRecover[B](v: Future[B])
      extends TransRecover[Recovered[B]] {
    override def transRecover(implicit ec: EC): FutureRecovered[B] =
      ValidationFuture.recover(v)(ec)
  }

}
