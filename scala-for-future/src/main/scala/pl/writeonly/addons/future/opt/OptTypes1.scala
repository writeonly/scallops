package pl.writeonly.addons.future.opt

import pl.writeonly.addons.future.Types1

import scala.concurrent.Future

trait OptTypes1 extends Types1 {
  type Value[A] = Option[Future[A]]
  type Result[A] = Future[Option[A]]
}
