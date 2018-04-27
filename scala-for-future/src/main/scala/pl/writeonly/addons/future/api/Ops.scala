package pl.writeonly.addons.future.api

import scala.concurrent.Future

object Ops {
  trait GetOrFailed[A] {
    def getOrFailed(implicit ec: EC): Future[A]
  }

  trait InSideOut[A] {
    def inSideOut(implicit ec: EC): Future[A]
  }

  trait Recover[A] {
    def recover(implicit ec: EC): Future[A]
  }

}
