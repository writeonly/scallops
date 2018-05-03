package pl.writeonly.addons.future.scalactic

import org.scalactic.{Fail, Pass, Validation}
import pl.writeonly.addons.future.api.Ops.{GetOrFailed, InSideOut, TransRecover}
import pl.writeonly.addons.future.api.{EC, Types1, Utils}

import scala.concurrent.Future

trait Validation0Future extends Types1 with Utils {

  override type Value[A] = Validation[A]

  override def getOrFailed[A](v: ValueFuture[A])(implicit ec: EC): Future[A] =
    ???

//  override def getOrFailed[A](v: ValueFuture[A])(implicit ec: EC): Future[A] =
//    v match {
//      case Pass            => Future.successful(Unit)
//      case Fail(a)               => a |> toThrowable |> Future.failed
//    }

  override def inSideOut[A](
    v: ValueFuture[A]
  )(implicit ec: EC): FutureValue[A] =
    v match {
      case Pass               => Future.successful(Pass)
      case Fail(f: Future[A]) => for (a <- f) yield Fail(a)
    }

  override def transRecover[A](
    v: Future[A]
  )(implicit ec: EC): FutureRecovered[A] =
    v.transformAndRecover(_ => Pass, { case (a: A) => Fail(a) })

  //    value.transform({
  //      case Success(s) => Success(Option(s))
  //      case Failure(_) => Success(None)
  //    })

  implicit class ValidationFutureGetOrFailed[A](v: ValueFuture[A])
      extends GetOrFailed[A] {
    override def getOrFailed(implicit ec: EC): Future[A] =
      Validation0Future.getOrFailed(v)(ec)
  }

  implicit class ValidationFutureInSideOut[A](v: ValueFuture[A])
      extends InSideOut[Value[A]] {
    override def inSideOut(implicit ec: EC): FutureValue[A] =
      Validation0Future.inSideOut(v)(ec)
  }

  implicit class ValidationFutureTransRecover[A](v: Future[A])
      extends TransRecover[Value[A]] {
    override def transRecover(implicit ec: EC): FutureRecovered[A] =
      Validation0Future.transRecover(v)(ec)
  }

}

object Validation0Future extends Validation0Future
