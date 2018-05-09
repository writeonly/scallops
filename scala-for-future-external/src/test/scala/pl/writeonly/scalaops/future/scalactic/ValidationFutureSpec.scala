package pl.writeonly.scalaops.future.scalactic

import org.scalactic.{Fail, Pass, Validation}
import pl.writeonly.scalaops.future.RemoteService.ClientException
import pl.writeonly.scalaops.future.{RemoteService, WhiteFutureSpecWithEither}
import pl.writeonly.scalaops.ops.ToThrowableException

import scala.runtime.BoxedUnit

class ValidationFutureSpec
    extends WhiteFutureSpecWithEither
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
      it("getOrFailed") {
        for {
          i <- v.getOrFailed
        } yield {
          i shouldBe a[BoxedUnit]
          i shouldBe ()
        }
      }
      it("getOrFailed and transRecover") {
        for {
          i <- v.getOrFailed.transRecover
        } yield {
          i shouldBe Pass
        }
      }
    }
    describe("for Fail") {
      val v: Validation[String] = Fail(RemoteService.InternalServerError)
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Fail(RemoteService.InternalServerError)
        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[ToThrowableException] {
          for {
            i <- v.getOrFailed
          } yield {
            i shouldBe RemoteService.InternalServerError
          }
        }
      }
      it("getOrFailed and transRecover") {
        for {
          i <- v.getOrFailed.transRecover
        } yield {
          i shouldBe a[Fail[ToThrowableException]]
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
          f shouldBe Fail(ClientException())
        }
      }
      it("for successful and failed") {
        for {
          s <- RemoteService.successful1.transRecover
          f <- RemoteService.failed0InternalServerError.transRecover
          l = List(s, f)
        } yield {
          s shouldBe Pass
          f shouldBe Fail(ClientException())
          l shouldBe List(Pass, Fail(ClientException()))
        }
      }

    }
  }

}
