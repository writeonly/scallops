package pl.writeonly.scalaops.scalactic

import org.scalactic.{Bad, Good, Or}
import pl.writeonly.scalaops.api.future.Ops.{FutureVOps, TransRecover}
import pl.writeonly.scalaops.api.future.{EC, TypesBoth, Utils}

import scala.concurrent.Future

trait OrFuture extends TypesBoth with Utils {

  override type Value[A, B] = B Or A

  override def getOrFailed[A, B](v: FutureV[A, B])(implicit ec: EC): Future[B] =
    v match {
      case Good(f: Future[B]) => f
      case Bad(f)             => f |> toThrowable[A] |> Future.failed
    }

  override def inSideOut[A, B](
    v: FutureV[A, B]
  )(implicit ec: EC): ValueF[A, B] =
    v match {
      case Good(f: Future[B]) => for (a <- f) yield Good(a)
      case a @ Bad(_)         => a |> Future.successful
    }

  def transRecover[A](v: Future[A])(implicit ec: EC): RecoveredF[A] =
    v.transformAndRecover(s => Good(s), Bad.apply)

  implicit class OrFutureInSideOut[B, A](v: FutureV[A, B])
      extends FutureVOps[Value[A, B], B] {
    override def inSideOut(implicit ec: EC): ValueF[A, B] =
      OrFuture.inSideOut(v)(ec)
    override def getOrFailed(implicit ec: EC): Future[B] =
      OrFuture.getOrFailed(v)(ec)
  }

  implicit class OrFutureTransRecover[A](f: Future[A])
      extends TransRecover[A, Recovered[A]](f) {
    override def transformSuccess: A => Recovered[A] = Good(_)

    override def recoverFailure: Throwable => Recovered[A] = Bad(_)
  }

}

object OrFuture extends OrFuture
