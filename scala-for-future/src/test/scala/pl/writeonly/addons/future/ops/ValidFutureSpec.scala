package pl.writeonly.addons.future.ops

import cats.data.Validated
import cats.data.Validated.Valid
import pl.writeonly.addons.future.ops.ValidFuture._
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future

class ValidFutureSpec extends WhiteFutureSpec {
  describe("A Valid") {
    val v: Validated[String, Future[Int]] =
      Valid(Future.successful(1))
    it("inSideOut") {
      for {
        a <- v.inSideOut
      } yield {
        a shouldBe Valid(1)
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
