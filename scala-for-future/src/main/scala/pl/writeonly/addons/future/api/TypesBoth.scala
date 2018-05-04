package pl.writeonly.addons.future.api

import scala.concurrent.Future

trait TypesBoth {

  type Value[A, B]
  type ValueF[A, B] = Future[Value[A, B]]
  type FutureV[A, B] = Value[A, Future[B]]
  type Recovered[B] = Value[Throwable, B]
  type RecoveredF[B] = Future[Recovered[B]]

  def inSideOut[A, B](v: FutureV[A, B])(implicit ec: EC): ValueF[A, B]

  def getOrFailed[A, B](v: FutureV[A, B])(implicit ec: EC): Future[B]

  def transRecover[A](value: Future[A])(implicit ec: EC): RecoveredF[A]

}
