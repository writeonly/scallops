package pl.writeonly.scalaops.monoid.impl

import pl.writeonly.scalaops.monoid.api.present.{PipeRightOps, ValueOpsLike}

import scala.util.{Failure, Success, Try}

trait OptOps {

  implicit class OptionOps[A](value: Option[A]) extends ValueOpsLike[A] {

    def toTry: Try[A] =
      value
        .map(Success(_))
        .getOrElse(Failure(toThrowable))

    override def mapOrElse[B](f: A => B)(b: B): B = value.map(f).getOrElse(b)

  }

  implicit class OptionPipeRightOps[A](a: A) extends PipeRightOps[A, Option] {
    def pipeFold[B](b: Option[B])(f: F[B]): A = b.fold(a)(f(a, _))

    def pipeMap[B](b: Option[B])(f: F[B]): A = b.mapOrElse(f(a, _))(a)
  }

}

object OptOps extends OptOps
