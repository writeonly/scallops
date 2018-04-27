package pl.writeonly.addons.future.ops

import pl.writeonly.addons.future.ops.OptFuture._
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future

class OptFutureSpec extends WhiteFutureSpec {
  describe("A Opt") {
    val v = Option(Future.successful(1))
    it("inSideOut") {
      for {
        a <- v.inSideOut
      } yield {
        a shouldBe Some(1)
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
