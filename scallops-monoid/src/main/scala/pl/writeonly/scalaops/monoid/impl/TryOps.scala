package pl.writeonly.scalaops.monoid.impl

import pl.writeonly.scalaops.monoid.api.present.{PipeRightOps, TryOpsLike}

import scala.concurrent.Future
import scala.util.Try

trait TryOps {

  implicit class TryOps[A](value: Try[A]) extends TryOpsLike[A] {
    def toFuture: Future[A] = value |> Future.fromTry

    override def mapOrElse[B](f: A => B)(b: B): B = value.map(f).getOrElse(b)

  }

  implicit class OptionPipeRightOps[A](a: A) extends PipeRightOps[A, Try] {
    def pipeFold[B](b: Try[B])(f: F[B]): A = b.fold(_ => a, f(a, _))

    def pipeMap[B](b: Try[B])(f: F[B]): A = b.mapOrElse(f(a, _))(a)
  }

}

object TryOps extends TryOps
