package pl.writeonly.scalaops.monoid.cats

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import pl.writeonly.scalaops.RemoteService.{ClientException, ResultF}
import pl.writeonly.scalaops.monoid.api.present.ToThrowableException
import pl.writeonly.scalaops.{RemoteService, WhiteFutureSpecWithEither}

import scala.concurrent.Future

class ValidatedFutureSpec
    extends WhiteFutureSpecWithEither
    with ValidatedFuture {
  describe("A Validated") {
    describe("for Valid with successful") {
      val v: Validated[String, ResultF] =
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
      val v: Validated[String, ResultF] =
        Validated.invalid(RemoteService.InternalServerError)
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Invalid(RemoteService.InternalServerError)
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
          i.toEither.left.value shouldBe a[ToThrowableException]
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
          f shouldBe Invalid(ClientException())
        }
      }
      it("for successful and failed") {
        for {
          s <- RemoteService.successful1.transRecover
          f <- RemoteService.failed0InternalServerError.transRecover
        } yield {
          s shouldBe Valid(1)
          f shouldBe Invalid(ClientException())
        }
      }
    }
  }

}
