package pl.writeonly.scalaops.ops.mono

import scala.util.Try

trait AnyOps {
  implicit class AnyOps[A](a: A) extends OptOps with TryOps with EitherOps {
    type F[B] = (A, B) => A

    def pipeFold[B](b: Option[B])(f: F[B]): A = b.fold(a)(f(a, _))

    def pipeFold[B](b: Try[B])(f: F[B]): A = b.fold(_ => a, f(a, _))

    def pipeFold[B](b: Either[_, B])(f: F[B]): A = b.fold(_ => a, f(a, _))

    def pipeMap[B](b: Option[B])(f: F[B]): A = b.mapOrElse(f(a, _))(a)

    def pipeMap[B](b: Try[B])(f: F[B]): A = b.mapOrElse(f(a, _))(a)

    def pipeMap[B](b: Either[_, B])(f: F[B]): A = b.mapOrElse(f(a, _))(a)
  }
}

object AnyOps extends AnyOps
