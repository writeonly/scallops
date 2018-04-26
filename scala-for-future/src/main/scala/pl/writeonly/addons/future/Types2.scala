package pl.writeonly.addons.future

import scala.concurrent.{ExecutionContext, Future}

trait Types2 {

  type Value[A, B]
  type Result[A, B]

  def inSideOut[A, B](value: Value[A, B])(implicit ec: ExecutionContext): Result[A, B]

  def getOrFailed[A, B](value: Value[A, B])(implicit ec: ExecutionContext): Future[B]


}
