package pl.writeonly.scalaops.api.future

import scala.concurrent.Future

trait TypesLeft {

  type Value[A]
  type ValueF[A] = Future[Value[A]]
  type FutureV[A] = Value[A]
  type Recovered = Value[Throwable]
  type RecoveredF = Future[Recovered]

  def inSideOut[A](v: FutureV[A])(implicit ec: EC): ValueF[A]

  def unitOrFailed[A](v: FutureV[A])(implicit ec: EC): Future[Unit]

  def neverOrFailed[A](v: FutureV[A])(implicit ec: EC): Future[Nothing]

  def getOrFailed[A, B](v: FutureV[A], b: B)(implicit ec: EC): Future[B]
}
