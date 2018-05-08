package pl.writeonly.scalaops.future.api

import scala.concurrent.Future

object Ops {
  trait GetOrFailed[A] {
    def getOrFailed(implicit ec: EC): Future[A]
  }

  trait InSideOut[A] {
    def inSideOut(implicit ec: EC): Future[A]
  }

  trait TransRecover[A] {
    def transRecover(implicit ec: EC): Future[A]
  }

}
