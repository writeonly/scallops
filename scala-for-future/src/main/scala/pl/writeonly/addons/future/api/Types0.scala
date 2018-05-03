package pl.writeonly.addons.future.api

import scala.concurrent.Future

trait Types0 {

  type Value[A]
  type ValueFuture[A] = Value[A]
  type FutureValue[A] = Future[Value[A]]
  type Recovered = Value[Throwable]
  type FutureRecovered = Future[Recovered]

  def inSideOut[A](value: ValueFuture[A])(implicit ec: EC): FutureValue[A]

  def getOrFailed[A](value: ValueFuture[A])(implicit ec: EC): Future[_]

  def recover[B](value: Future[B])(implicit ec: EC): FutureRecovered

}
