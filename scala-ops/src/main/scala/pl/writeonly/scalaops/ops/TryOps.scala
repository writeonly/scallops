package pl.writeonly.scalaops.ops

import scala.concurrent.Future
import scala.util.Try

trait TryOps {
  implicit class TryOps[A](value: Try[A]) extends TryOpsLike[A] {
    def toFuture: Future[A] = value |> Future.fromTry
  }
}

object TryOps extends TryOps
