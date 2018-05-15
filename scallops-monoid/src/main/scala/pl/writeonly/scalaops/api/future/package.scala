package pl.writeonly.scalaops.api

import scala.concurrent.{ExecutionContext, Future}

package object future {
  type EC = ExecutionContext
  type F[A] = Future[A]
}
