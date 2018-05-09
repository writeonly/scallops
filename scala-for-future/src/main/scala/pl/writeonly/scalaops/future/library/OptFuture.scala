package pl.writeonly.scalaops.future.library

import pl.writeonly.scalaops.future.api.Ops.{
  GetOrFailed,
  InSideOut,
  TransRecover
}
import pl.writeonly.scalaops.future.api.{EC, TypesRight, Utils}
import pl.writeonly.scalaops.ops.ToThrowableException.ToThrowable0Exception

import scala.concurrent.Future

trait OptFuture extends TypesRight with Utils {

  override type Value[A] = Option[A]

  override def getOrFailed[A](v: FutureV[A])(implicit ec: EC): Future[A] =
    v match {
      case Some(f: Future[A]) => f
      case None               => ToThrowable0Exception() |> Future.failed
    }

  override def inSideOut[A](v: FutureV[A])(implicit ec: EC): ValueF[A] =
    v match {
      case Some(f: Future[A]) => for (a <- f) yield Option(a)
      case None               => None |> Future.successful
    }

  override def transRecover[A](v: Future[A])(implicit ec: EC): RecoveredF[A] =
    v.transformAndRecover(Option.apply, _ => Option.empty)

  //    value.transform({
  //      case Success(s) => Success(Option(s))
  //      case Failure(_) => Success(None)
  //    })

  implicit class OptFutureGetOrFailed[A](v: FutureV[A]) extends GetOrFailed[A] {
    override def getOrFailed(implicit ec: EC): Future[A] =
      OptFuture.getOrFailed(v)(ec)
  }

  implicit class OptFutureInSideOut[A](v: FutureV[A])
      extends InSideOut[Value[A]] {
    override def inSideOut(implicit ec: EC): ValueF[A] =
      OptFuture.inSideOut(v)(ec)
  }

  implicit class OptFutureTransRecover[A](v: Future[A])
      extends TransRecover[Value[A]] {
    override def transRecover(implicit ec: EC): RecoveredF[A] =
      OptFuture.transRecover(v)(ec)
  }

}

object OptFuture extends OptFuture
