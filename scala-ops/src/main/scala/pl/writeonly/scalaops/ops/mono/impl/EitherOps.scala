package pl.writeonly.scalaops.ops.mono.impl

import pl.writeonly.scalaops.ops.mono.api.{
  PipeBothOps,
  PipeRightOps,
  ValueOpsLike
}

import scala.util.{Failure, Success, Try}

trait EitherOps {

  implicit class EitherOps[B, A](value: Either[B, A]) extends ValueOpsLike[A] {
    def toTry: Try[A] = value match {
      case Right(a) => a |> Success[A]
      case Left(b)  => b |> toThrowable[B] |> Failure[A]
    }

    override def mapOrElse[C](f: A => C)(b: C): C = value.map(f).getOrElse(b)
  }

//  implicit class EitherPipeRightOps[A](a: A) extends PipeBothOps[A, Either, ]{
//    override def pipeFold[B, C](b: Either[C, B])(f: F[B]): A =  b.fold(_ => a, f(a, _))
//
//    override def pipeMap[B, C](b: Either[C, B])(f: F[B]): A = b.mapOrElse(f(a, _))(a)
//  }

}

object EitherOps extends EitherOps
