package pl.writeonly.scalaops.future.scalaz

import pl.writeonly.scalaops.future.api.Ops.{
  GetOrFailed,
  InSideOut,
  TransRecover
}
import pl.writeonly.scalaops.future.api.{EC, TypesRight, Utils}
import pl.writeonly.scalaops.future.api.{TypesRight, Utils}
import pl.writeonly.scalaops.ops.ToThrowableException.ToThrowable0Exception
import scalaz.Maybe
import scalaz.Maybe.{Empty, Just}

import scala.concurrent.Future

trait MaybeFuture extends TypesRight with Utils {
  override type Value[A] = Maybe[A]

  override def getOrFailed[A](v: FutureV[A])(implicit ec: EC): Future[A] =
    v match {
      case Just(f: Future[A]) => f
      case Empty()            => ToThrowable0Exception() |> Future.failed
    }

  override def inSideOut[A](v: FutureV[A])(implicit ec: EC): ValueF[A] =
    v match {
      case Just(f: Future[A]) => for (a <- f) yield Maybe.fromNullable(a)
      case a: Empty[A]        => Future.successful(a)
    }

  override def transRecover[A](v: Future[A])(implicit ec: EC): RecoveredF[A] =
    v.transformAndRecover((s: A) => Just(s), { case _ => Maybe.empty })

  //    value.transform({
  //      case Success(s) => Success(Option(s))
  //      case Failure(_) => Success(Empty())
  //    })

  implicit class OptFutureGetOrFailed[A](v: FutureV[A]) extends GetOrFailed[A] {
    override def getOrFailed(implicit ec: EC): Future[A] =
      MaybeFuture.getOrFailed(v)(ec)
  }

  implicit class OptFutureInSideOut[A](v: FutureV[A])
      extends InSideOut[Value[A]] {
    override def inSideOut(implicit ec: EC): ValueF[A] =
      MaybeFuture.inSideOut(v)(ec)
  }

  implicit class OptFutureTransRecover[A](v: Future[A])
      extends TransRecover[Value[A]] {
    override def transRecover(implicit ec: EC): RecoveredF[A] =
      MaybeFuture.transRecover(v)(ec)
  }
}

object MaybeFuture extends MaybeFuture
