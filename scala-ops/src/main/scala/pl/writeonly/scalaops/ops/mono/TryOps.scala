package pl.writeonly.scalaops.ops.mono

import scala.concurrent.Future
import scala.util.Try

trait TryOps {

  implicit class TryOps[A](value: Try[A]) extends TryOpsLike[A] {
    def toFuture: Future[A] = value |> Future.fromTry

    override def mapOrElse[B](f: A => B)(b: B): B = value.map(f).getOrElse(b)

  }

}

object TryOps extends TryOps
