package pl.writeonly.addons.future.valid

import cats.data.Validated
import pl.writeonly.addons.future.Types2

import scala.concurrent.Future

class ValidTypes2 extends Types2 {
  type Value[A, B] = Validated[A, Future[B]]
  type Result[A, B] = Future[Validated[A, B]]
}
