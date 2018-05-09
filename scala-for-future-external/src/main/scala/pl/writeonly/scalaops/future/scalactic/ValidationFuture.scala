package pl.writeonly.scalaops.future.scalactic

import org.scalactic.{Fail, Pass, Validation}
import pl.writeonly.scalaops.future.api.Ops.{
  GetOrFailed,
  InSideOut,
  TransRecover
}
import pl.writeonly.scalaops.future.api.{EC, TypesLeft, Utils}

import scala.concurrent.Future

trait ValidationFuture extends TypesLeft with Utils {

  override type Value[A] = Validation[A]

  override def getOrFailed[A](v: FutureV[A])(implicit ec: EC): Future[Unit] =
    v match {
      case Pass       => Future.unit
      case Fail(f: A) => f |> toThrowable[A] |> Future.failed
    }

  override def inSideOut[A](v: FutureV[A])(implicit ec: EC): ValueF[A] =
    v match {
      case Pass        => Future.successful(null)
      case a @ Fail(_) => a |> Future.successful
    }

  //  override def recover[A](v: Future[A])(implicit ec: EC): Recovered[A] = ???

  def transRecover[A](v: Future[A])(implicit ec: EC): RecoveredF =
    v.transformAndRecover(_ => Pass, { case t => Fail(t) })

  implicit class OrFutureInSideOut[A](v: FutureV[A])
      extends InSideOut[Value[A]] {
    override def inSideOut(implicit ec: EC): ValueF[A] =
      ValidationFuture.inSideOut(v)(ec)
  }

  implicit class OrFutureGetOrFailed[A](v: FutureV[A])
      extends GetOrFailed[Unit] {
    override def getOrFailed(implicit ec: EC): Future[Unit] =
      ValidationFuture.getOrFailed(v)(ec)
  }

  implicit class OrFutureTransRecover[A](value: Future[A])
      extends TransRecover[Recovered] {

    override def transRecover(implicit ec: EC): RecoveredF =
      ValidationFuture.transRecover(value)(ec)
  }

}

object ValidationFuture extends ValidationFuture
