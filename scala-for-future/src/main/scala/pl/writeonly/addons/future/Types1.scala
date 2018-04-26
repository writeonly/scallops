package pl.writeonly.addons.future

import scala.concurrent.{ExecutionContext, Future}

trait Types1 {
  type Value[A]
  type Result[A]

  def inSideOut[A](value: Value[A])(implicit ec: ExecutionContext): Result[A]

  def getOrFailed[A](value: Value[A])(implicit ec: ExecutionContext): Future[A]
}
