package pl.writeonly.addons.future.`with`

import pl.writeonly.addons.future.Types1

import scala.concurrent.Future
import scala.util.Try

trait TryTypes1 extends Types1 {
  type Value[A] = Try[Future[A]]
  type Result[A] = Future[Try[A]]
}
