package pl.writeonly.addons.future.cats

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import pl.writeonly.addons.future.{CaseException, RemoteService}
import pl.writeonly.addons.future.cats.ValidatedFuture._
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future

class ValidatedFutureSpec extends WhiteFutureSpec {
  describe("A Validated") {
    describe("for Valid with successful") {
      val v: Validated[String, Future[Int]] =
        Valid(Future.successful(1))
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
          f <- RemoteService.failed.transRecover
        } yield {
          f shouldBe Invalid(CaseException())
        }
      }
      it("for successful and failed") {
        for {
          s <- RemoteService.successful1.transRecover
          f <- RemoteService.failed.transRecover
        } yield {
          s shouldBe Valid(1)
          f shouldBe Invalid(CaseException())
        }
      }
    }
  }

}
