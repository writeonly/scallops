package pl.writeonly.addons.future.api

import scala.concurrent.Future

trait TypesRight {

  type Value[A]
  type ValueF[A] = Future[Value[A]]
  type FutureV[A] = Value[Future[A]]
  type Recovered[A] = Value[A]
  type RecoveredF[A] = Future[Recovered[A]]

  def inSideOut[A](v: FutureV[A])(implicit ec: EC): ValueF[A]

  def getOrFailed[A](v: FutureV[A])(implicit ec: EC): Future[A]

  def transRecover[A](v: Future[A])(implicit ec: EC): RecoveredF[A]
}
