package pl.writeonly.addons.future.scalactic

import org.scalactic.{Bad, Good, Or}
import pl.writeonly.addons.future.api.Ops.{GetOrFailed, InSideOut, TransRecover}
import pl.writeonly.addons.future.api.{EC, Types2, Utils}
import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future

object OrFuture extends Types2 with Utils {

  override type Value[A, B] = B Or A

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
      case a: Bad[A]          => a |> Future.successful
    }

//  override def recover[A](v: Future[A])(implicit ec: EC): Recovered[A] = ???

  def recover[A](v: Future[A])(implicit ec: EC): FutureRecovered[A] =
    transform(v, (s: A) => Good(s), { case t => Bad(t) })

  implicit class OrFutureInSideOut[B, A](v: Future[B] Or A)
      extends InSideOut[Value[A, B]] {
    override def inSideOut(implicit ec: EC): FutureValue[A, B] =
      OrFuture.inSideOut(v)(ec)
  }

  implicit class OrFutureGetOrFailed[A, B](v: ValueFuture[A, B])
      extends GetOrFailed[B] {
    override def getOrFailed(implicit ec: EC): Future[B] =
      OrFuture.getOrFailed(v)(ec)
  }

  implicit class OrFutureTransRecover[A](value: Future[A])
      extends TransRecover[Recovered[A]] {

    override def transRecover(implicit ec: EC): Future[A Or Throwable] =
      OrFuture.recover(value)(ec)
  }

}
