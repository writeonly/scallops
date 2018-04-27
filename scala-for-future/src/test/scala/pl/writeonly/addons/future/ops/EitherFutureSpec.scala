package pl.writeonly.addons.future.ops

import pl.writeonly.addons.future.ops.EitherFuture._
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future

class EitherFutureSpec extends WhiteFutureSpec {
  describe("A Either") {
    val v = Right[String, Future[Int]](Future.successful(1))
    it("inSideOut") {
      for {
        a <- v.inSideOut
      } yield {
        a shouldBe Right(1)
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
