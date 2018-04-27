package pl.writeonly.addons.future.api

import scala.concurrent.Future

trait Types2 {

  type Value[A, B]
  type ValueFuture[A, B] = Value[A, Future[B]]
  type FutureValue[A, B] = Future[Value[A, B]]
  type Recovered[B] = Value[Throwable, B]
  type FutureRecovered[B] = Future[Recovered[B]]

  def inSideOut[A, B](value: ValueFuture[A, B])(
    implicit ec: EC
  ): FutureValue[A, B]

  def getOrFailed[A, B](value: ValueFuture[A, B])(implicit ec: EC): Future[B]

  def recover[A](value: Future[A])(implicit ec: EC): FutureRecovered[A]

}
