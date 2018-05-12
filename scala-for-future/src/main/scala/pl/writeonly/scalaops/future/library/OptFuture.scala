package pl.writeonly.scalaops.future.library

import pl.writeonly.scalaops.future.api.Ops.{FutureVOps, TransRecover}
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

  implicit class OptFutureVOps[A](v: FutureV[A])
      extends FutureVOps[Value[A], A] {
    override def inSideOut(implicit ec: EC): ValueF[A] =
      OptFuture.inSideOut(v)(ec)

    override def getOrFailed(implicit ec: EC): Future[A] =
      OptFuture.getOrFailed(v)(ec)
  }

  implicit class OptFutureTransRecover[A](f: Future[A])
      extends TransRecover[A, Recovered[A]](f) {
    override def transformSuccess = Option.apply

    override def recoverFailure: Throwable => Recovered[A] = _ => Option.empty
  }

}

object OptFuture extends OptFuture
