package pl.writeonly.addons.future

import scala.concurrent.{ExecutionContext, Future}

trait Types2 {

  type Value[A, B]
  type Result[A, B]
  type Recovered[A]

  def inSideOut[A, B](value: Value[A, B])(implicit ec: ExecutionContext): Result[A, B]

  def getOrFailed[A, B](value: Value[A, B])(implicit ec: ExecutionContext): Future[B]

  def recover[A](value: Future[A])(implicit ec: ExecutionContext): Recovered[A]

}
