package pl.writeonly.scalaops.api.future

import scala.concurrent.Future

trait TypesBoth {

  type Value[A, B]
  type ValueF[A, B] = Future[Value[A, B]]
  type FutureV[A, B] = Value[A, Future[B]]
  type Recovered[B] = Value[Throwable, B]
  type RecoveredF[B] = Future[Recovered[B]]

  type TransformSuccess[B] = B => Recovered[B]
  type RecoverFailure[B] = Throwable => Recovered[B]

  def inSideOut[A, B](v: FutureV[A, B])(implicit ec: EC): ValueF[A, B]

  def getOrFailed[A, B](v: FutureV[A, B])(implicit ec: EC): Future[B]

}
