package pl.writeonly.addons.future.either

import pl.writeonly.addons.future.Types2

import scala.concurrent.Future

trait EitherTypes2 extends Types2 {
  type Value[A, B] = Either[A, Future[B]]
  type Result[A, B] = Future[Either[A, B]]

}
