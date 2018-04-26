package pl.writeonly.addons.future.or

import org.scalactic.Or
import pl.writeonly.addons.future.Types2

import scala.concurrent.Future

trait OrTypes2 extends Types2 {
  type Value[A, B] = Or[Future[B], A]
  type Result[A, B] = Future[B Or A]

}
