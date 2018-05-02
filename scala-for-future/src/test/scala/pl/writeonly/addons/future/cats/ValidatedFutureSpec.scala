package pl.writeonly.addons.future.cats

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import org.scalatest.EitherValues
import pl.writeonly.addons.future.RemoteService
import pl.writeonly.addons.future.RemoteService.{CaseException, FutureResult}
import pl.writeonly.addons.future.cats.ValidatedFuture._
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future

class ValidatedFutureSpec extends WhiteFutureSpec with EitherValues {
  describe("A Validated") {
    describe("for Valid with successful") {
      val v: Validated[String, FutureResult] =
        Validated.valid(Future.successful(1))
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Valid(1)
        }
      }
      it("getOrFailed") {
        for {
          i <- v.getOrFailed
        } yield {
          i shouldBe 1
        }
      }
      it("getOrFailed and transRecover") {
        for {
          i <- v.getOrFailed.transRecover
        } yield {
          i shouldBe Valid(1)
        }
      }
    }
    describe("for Invalid") {
      val v: Validated[String, FutureResult] =
        Validated.invalid(CaseException().message)
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Invalid(CaseException().message)
        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[IllegalStateException] {
          for {
            i <- v.getOrFailed
          } yield {
            i shouldBe CaseException().message
          }
        }
      }
      it("getOrFailed and transRecover") {
        for {
          i <- v.getOrFailed.transRecover
        } yield {
          i.toEither.left.value shouldBe a[IllegalStateException]
        }
      }
    }
    describe("transRecover") {
      it("for successful") {
        for {
          s <- RemoteService.successful1.transRecover
        } yield {
          s shouldBe Valid(1)
        }
      }
      it("for failed") {
        for {
          f <- RemoteService.failed0InternalServerError.transRecover
        } yield {
          f shouldBe Invalid(CaseException())
        }
      }
      it("for successful and failed") {
        for {
          s <- RemoteService.successful1.transRecover
          f <- RemoteService.failed0InternalServerError.transRecover
        } yield {
          s shouldBe Valid(1)
          f shouldBe Invalid(CaseException())
        }
      }
    }
  }

}
