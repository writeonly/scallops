package pl.writeonly.addons.future.api

import scala.concurrent.{ExecutionContext, Future}

trait Types1 {

  type Value[A]
  type ValueFuture[A] = Value[Future[A]]
  type FutureValue[A] = Future[Value[A]]
  type Recovered[A] = Value[A]
  type FutureRecovered[A] = Future[Recovered[A]]

  def inSideOut[A](value: ValueFuture[A])(
    implicit ec: ExecutionContext
  ): FutureValue[A]

  def getOrFailed[A](value: ValueFuture[A])(
    implicit ec: ExecutionContext
  ): Future[A]

  def transRecover[A](value: Future[A])(
    implicit ec: ExecutionContext
  ): FutureRecovered[A]
}
