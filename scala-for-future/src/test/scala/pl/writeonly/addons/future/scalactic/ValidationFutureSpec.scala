package pl.writeonly.addons.future.scalactic

import org.scalactic.{Fail, Pass, Validation}
import org.scalatest.EitherValues
import pl.writeonly.addons.future.RemoteService
import pl.writeonly.addons.future.RemoteService.CaseException
import pl.writeonly.sons.specs.WhiteFutureSpec

class ValidationFutureSpec
    extends WhiteFutureSpec
    with EitherValues
    with ValidationFuture {
  describe("A Validation") {
    describe("for Pass") {
      val v: Validation[String] = Pass
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe null
        }
      }
      ignore("getOrFailed") {
        recoverToSucceededIf[NotImplementedError] {
          for {
            i <- v.getOrFailed
          } yield {
            i shouldBe null
          }
        }
      }
      ignore("getOrFailed and transRecover") {
        recoverToSucceededIf[NotImplementedError] {
          for {
            i <- v.getOrFailed.transRecover
          } yield {
            i shouldBe Pass
          }
        }
      }
    }
    describe("for Fail") {
      val v: Validation[String] = Fail(CaseException().message)
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Fail(CaseException().message)
        }
      }
      ignore("getOrFailed") {
        recoverToSucceededIf[NotImplementedError] {
          for {
            i <- v.getOrFailed
          } yield {
            i shouldBe CaseException().message
          }
        }
      }
      ignore("getOrFailed and transRecover") {
        recoverToSucceededIf[NotImplementedError] {
          for {
            i <- v.getOrFailed.transRecover
          } yield {
            i shouldBe a[IllegalStateException]
          }
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
      it("for successful and failed") {
        for {
          s <- RemoteService.successful1.transRecover
          f <- RemoteService.failed0InternalServerError.transRecover
          l = List(s, f)
        } yield {
          s shouldBe Pass
          f shouldBe Fail(CaseException())
          l shouldBe List(Pass, Fail(CaseException()))
        }
      }

    }
  }

}
