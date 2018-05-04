package pl.writeonly.addons.future.api

import scala.concurrent.Future

trait TypesLeft {

  type Value[A]
  type ValueF[A] = Future[Value[A]]
  type FutureV[A] = Value[A]
  type Recovered = Value[Throwable]
  type RecoveredF = Future[Recovered]

  def inSideOut[A](v: FutureV[A])(implicit ec: EC): ValueF[A]

  def getOrFailed[A](v: FutureV[A])(implicit ec: EC): Future[Nothing]

  def transRecover[B](v: Future[B])(implicit ec: EC): RecoveredF

}
