package pl.writeonly.scalaops.future

import scala.concurrent.{ExecutionContext, Future}

package object api {
  type EC = ExecutionContext
  type F[A] = Future[A]
}
