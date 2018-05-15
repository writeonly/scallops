package pl.writeonly.scalaops.monoid.api

import scala.concurrent.{ExecutionContext, Future}

package object future {
  type EC = ExecutionContext
  type F[A] = Future[A]
}
