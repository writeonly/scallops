package pl.writeonly.addons.future.library

import pl.writeonly.addons.future.RemoteService
import pl.writeonly.addons.future.RemoteService.FutureResult
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future

class OptFutureSpec extends WhiteFutureSpec with OptFuture {
  describe("A Opt") {
    describe("for Some with successful") {
      val v: Option[FutureResult] = Option(Future.successful(1))
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Some(1)
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
          i shouldBe Some(1)
        }
      }
    }
    describe("for None") {
      val v: Option[FutureResult] = Option.empty
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe None
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
          i shouldBe None
        }
      }
    }
    describe("transRecover") {
      it("for successful") {
        for {
          s <- RemoteService.successful1.transRecover
        } yield {
          s shouldBe Some(1)
        }
      }
      it("for failed") {
        for {
          f <- RemoteService.failed0InternalServerError.transRecover
        } yield {
          f shouldBe None
        }
      }
    }

  }

}