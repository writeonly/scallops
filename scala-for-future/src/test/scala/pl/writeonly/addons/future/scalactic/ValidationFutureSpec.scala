package pl.writeonly.addons.future.scalactic

import org.scalactic.{Fail, Pass, Validation}
import pl.writeonly.addons.future.RemoteService
import pl.writeonly.addons.future.RemoteService.{CaseException, FutureResult}
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future

class ValidationFutureSpec extends WhiteFutureSpec with ValidationFuture {
  describe("A Validation") {
    describe("for Some with successful") {
      val v: Validation[FutureResult] = Fail(Future.successful(1))
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Fail(1)
        }
      }
      ignore("getOrFailed") {
        for {
          i <- v.getOrFailed
        } yield {
          i shouldBe 1
        }
      }
      ignore("transRecover") {
        for {
          i <- v.getOrFailed.transRecover
        } yield {
          i shouldBe Fail(1)
        }
      }
    }
    describe("for Pass") {
      val v: Validation[FutureResult] = Pass
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Pass
        }
      }
      ignore("getOrFailed") {
        recoverToSucceededIf[IllegalStateException] {
          for {
            i <- v.getOrFailed
          } yield i

        }
      }
      ignore("transRecover") {
        for {
          i <- v.getOrFailed.transRecover
        } yield {
          i shouldBe Pass
        }
      }
    }
    describe("transRecover") {
      it("for successful") {
        for {
          s <- RemoteService.successful1.transRecover
        } yield {
          s shouldBe Pass
        }
      }
      it("for failed") {
        for {
          f <- RemoteService.failed0InternalServerError.transRecover
        } yield {
          f shouldBe Fail(CaseException())
        }
      }
    }

  }

}
