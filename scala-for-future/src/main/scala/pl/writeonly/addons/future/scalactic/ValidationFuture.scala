package pl.writeonly.addons.future.scalactic

import org.scalactic.{Fail, Pass, Validation}
import pl.writeonly.addons.future.api.Ops.{GetOrFailed, InSideOut, TransRecover}
import pl.writeonly.addons.future.api.{EC, Types0, Utils}

import scala.concurrent.Future

trait ValidationFuture extends Types0 with Utils {

  override type Value[A] = Validation[A]

  override def getOrFailed[A](v: ValueFuture[A])(implicit ec: EC): Future[A] =
    ???

  override def inSideOut[A](
    v: ValueFuture[A]
  )(implicit ec: EC): FutureValue[A] =
    v match {
      case Pass        => Future.successful(null)
      case a @ Fail(_) => a |> Future.successful
    }

//  override def recover[A](v: Future[A])(implicit ec: EC): Recovered[A] = ???

  def recover[A](v: Future[A])(implicit ec: EC): FutureRecovered =
    v.transformAndRecover(_ => Pass, { case t => Fail(t) })

  implicit class OrFutureInSideOut[A](v: ValueFuture[A])
      extends InSideOut[Value[A]] {
    override def inSideOut(implicit ec: EC): FutureValue[A] =
      ValidationFuture.inSideOut(v)(ec)
  }

  implicit class OrFutureGetOrFailed[A](v: ValueFuture[A])
      extends GetOrFailed[A] {
    override def getOrFailed(implicit ec: EC): Future[A] =
      ValidationFuture.getOrFailed(v)(ec)
  }

  implicit class OrFutureTransRecover[A](value: Future[A])
      extends TransRecover[Recovered] {

    override def transRecover(implicit ec: EC): FutureRecovered =
      ValidationFuture.recover(value)(ec)
  }

}

object ValidationFuture extends ValidationFuture
