package pl.writeonly.scalaops.future.library

import pl.writeonly.scalaops.future.library.SeqFuture._
import pl.writeonly.scalaops.specs.WhiteFutureSpec

import scala.concurrent.Future

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
