package pl.writeonly.scalaops.ops.mono

import pl.writeonly.scalaops.ops.ToThrowable
import pl.writeonly.scalaops.pipe.Pipe

import scala.concurrent.Future
import scala.util.Try

trait ToFuture[A] {
  def toFuture: Future[A]
}

trait MapOrElse[A] {
  def mapOrElse[B](f: A => B)(b: B): B
}

trait TryOpsLike[A] extends ToFuture[A] with MapOrElse[A] with Pipe

trait ToTry[A] {
  def toTry: Try[A]
}

trait ToTryToFuture[A] extends TryOpsLike[A] with ToTry[A] with TryOps {
  def toFuture = toTry.toFuture
}

trait ValueOpsLike[A] extends ToTryToFuture[A] with ToThrowable
