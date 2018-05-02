package pl.writeonly.addons.future.scalactic

import org.scalactic._
import pl.writeonly.addons.future.api.Ops.{GetOrFailed, InSideOut, TransRecover}
import pl.writeonly.addons.future.api.{EC, Types2, Utils}

import scala.concurrent.Future

object OrEveryFuture extends Types2 with Utils {

  override type Value[A, B] = B Or Every[A]

  override def getOrFailed[A, B](
    v: ValueFuture[A, B]
  )(implicit ec: EC): Future[B] =
    v match {
      case Good(f: Future[B]) => f
      case Bad(f)             => f |> toThrowable |> Future.failed
    }

  override def inSideOut[A, B](
    v: ValueFuture[A, B]
  )(implicit ec: EC): FutureValue[A, B] =
    v match {
      case Good(f: Future[B]) => for (a <- f) yield Good(a)
      case a @ Bad(_)         => a |> Future.successful
    }

//  override def recover[A](v: Future[A])(implicit ec: EC): Recovered[A] = ???

  def recover[A](v: Future[A])(implicit ec: EC): FutureRecovered[A] =
    v.transformAndRecover((s: A) => Good(s), { case t => Bad(One(t)) })

  implicit class OrEveryFutureInSideOut[B, A](v: ValueFuture[A, B])
      extends InSideOut[Value[A, B]] {
    override def inSideOut(implicit ec: EC): FutureValue[A, B] =
      OrEveryFuture.inSideOut[A, B](v)(ec)
  }

  implicit class OrEveryFutureGetOrFailed[A, B](v: ValueFuture[A, B])
      extends GetOrFailed[B] {
    override def getOrFailed(implicit ec: EC): Future[B] =
      OrEveryFuture.getOrFailed(v)(ec)
  }

  implicit class OrEveryFutureTransRecover[A](value: Future[A])
      extends TransRecover[Recovered[A]] {

    override def transRecover(implicit ec: EC): FutureRecovered[A] =
      OrEveryFuture.recover(value)(ec)
  }

}
