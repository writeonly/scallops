package pl.writeonly.addons.future.ops

import org.scalactic.{ErrorMessage, Good, Or}
import pl.writeonly.addons.future.ops.OrFuture._
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future

class OrFutureSpec extends WhiteFutureSpec {
  describe("A Or") {

    val v: Future[Int] Or ErrorMessage =
      Good(Future.successful(1))

    it("inSideOut") {
      for {
        a <- v.inSideOut
      } yield {
        a shouldBe Good(1)
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
