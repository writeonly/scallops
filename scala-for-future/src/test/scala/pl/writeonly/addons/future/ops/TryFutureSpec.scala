package pl.writeonly.addons.future.ops

import pl.writeonly.addons.future.ops.TryFuture._
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future
import scala.util.{Success, Try}

class TryFutureSpec extends WhiteFutureSpec {
  describe("A Try") {

    val v = Try(Future.successful(1))
    it("inSideOut") {
      for {
        a <- v.inSideOut
      } yield {
        a shouldBe Success(1)
      }
    }
    it("getOrFailed") {
      for {
        a <- v.getOrFailed
      } yield {
        a shouldBe 1
      }
    }
  }

}
