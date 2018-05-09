package pl.writeonly.scalaops.future.scalaz

import pl.writeonly.scalaops.future.api.Ops.{
  GetOrFailed,
  InSideOut,
  TransRecover
}
import pl.writeonly.scalaops.future.api.{EC, TypesBoth, Utils}
import scalaz.{Failure, Success, Validation}

import scala.concurrent.Future

trait ValidationFuture extends TypesBoth with Utils {

  override type Value[A, B] = Validation[A, B]

  override def inSideOut[A, B](
    v: FutureV[A, B]
  )(implicit ec: EC): ValueF[A, B] =
    v match {
      case Success(f: Future[B]) => for (a <- f) yield Validation.success(a)
      case a: Failure[A]         => a |> Future.successful
    }

  override def getOrFailed[A, B](v: FutureV[A, B])(implicit ec: EC): Future[B] =
    v match {
      case Success(f: Future[B]) => f
      case a: Failure[A]         => a |> toThrowable[Failure[A]] |> Future.failed
    }

  override def transRecover[B](v: Future[B])(implicit ec: EC): RecoveredF[B] =
    v.transformAndRecover((s: B) => Success(s), {
      case t => Validation.failure(t)
    })

  //    value.transform({
  //      case Success(s) => Success(Good(s))
  //      case Failure(t) => Success(Bad(t))
  //    })

  implicit class SuccessFutureInSideOut[A, B](v: FutureV[A, B])
      extends InSideOut[Value[A, B]] {
    override def inSideOut(implicit ec: EC): ValueF[A, B] =
      ValidationFuture.inSideOut(v)(ec)
  }

  implicit class SuccessFutureGetOrFailed[A, B](v: FutureV[A, B])
      extends GetOrFailed[B] {
    override def getOrFailed(implicit ec: EC): Future[B] =
      ValidationFuture.getOrFailed(v)(ec)
  }

  implicit class SuccessFutureTransRecover[B](v: Future[B])
      extends TransRecover[Recovered[B]] {
    override def transRecover(implicit ec: EC): RecoveredF[B] =
      ValidationFuture.transRecover(v)(ec)
  }

}

object ValidationFuture extends ValidationFuture
