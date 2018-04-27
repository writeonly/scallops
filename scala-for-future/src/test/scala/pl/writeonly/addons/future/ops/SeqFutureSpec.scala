package pl.writeonly.addons.future.ops

import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future

import SeqFuture._

class SeqFutureSpec extends WhiteFutureSpec {
  describe("A Seq") {
    it("inSideOut") {
      for {
        a <- Seq(Future.successful(1), Future.successful(2)).inSideOut
      } yield {
        a shouldBe Seq(1, 2)
      }
    }
  }

}
