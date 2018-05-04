package pl.writeonly.addons.ops

import pl.writeonly.addons.pipe.Pipe

import scala.concurrent.Future
import scala.util.Try

trait ToFuture[A] {
  def toFuture: Future[A]
}

trait TryOpsLike[A] extends ToFuture[A] with Pipe

trait ToTry[A] {
  def toTry: Try[A]
}

trait ToTryToFuture[A] extends TryOpsLike[A] with ToTry[A] with TryOps {
  def toFuture = toTry.toFuture
}

trait ValueOpsLike[A] extends ToTryToFuture[A] with ToThrowable
