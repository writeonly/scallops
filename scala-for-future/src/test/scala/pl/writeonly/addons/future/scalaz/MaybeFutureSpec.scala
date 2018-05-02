package pl.writeonly.addons.future.scalaz

import pl.writeonly.addons.future.RemoteService
import pl.writeonly.addons.future.RemoteService.FutureResult
import pl.writeonly.addons.future.scalaz.MaybeFuture._
import pl.writeonly.sons.specs.WhiteFutureSpec
import scalaz.Maybe
import scalaz.Maybe.{Empty, Just}

import scala.concurrent.Future

class MaybeFutureSpec extends WhiteFutureSpec {
  describe("A Maybe") {
    describe("for Just with successful") {
      val v: Maybe[FutureResult] = Maybe.just(Future.successful(1))
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Just(1)
        }
      }
      it("getOrFailed") {
        for {
          i <- v.getOrFailed
        } yield {
          i shouldBe 1
        }
      }
      it("transRecover") {
        for {
          i <- v.getOrFailed.transRecover
        } yield {
          i shouldBe Just(1)
        }
      }
    }
    describe("for Empty") {
      val v: Maybe[FutureResult] = Maybe.empty[FutureResult]
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Empty()
        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[IllegalStateException] {
          for {
            i <- v.getOrFailed
          } yield i

        }
      }
      it("transRecover") {
        for {
          i <- v.getOrFailed.transRecover
        } yield {
          i shouldBe Empty()
        }
      }
    }
    describe("transRecover") {
      it("for successful") {
        for {
          s <- RemoteService.successful1.transRecover
        } yield {
          s shouldBe Just(1)
        }
      }
      it("for failed") {
        for {
          f <- RemoteService.failed0InternalServerError.transRecover
        } yield {
          f shouldBe Empty()
        }
      }
    }

  }

}
