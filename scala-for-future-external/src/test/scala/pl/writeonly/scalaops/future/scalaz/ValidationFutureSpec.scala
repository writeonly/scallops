package pl.writeonly.scalaops.future.scalaz

import pl.writeonly.scalaops.future.RemoteService.{ClientException, ResultF}
import pl.writeonly.scalaops.future.WhiteFutureSpecWithEither
import pl.writeonly.scalaops.future.{RemoteService, WhiteFutureSpecWithEither}
import pl.writeonly.scalaops.ops.ToThrowableException
import scalaz.{Failure, Success, Validation}

import scala.concurrent.Future

class ValidationFutureSpec
    extends WhiteFutureSpecWithEither
    with ValidationFuture {
  describe("A Validation") {
    describe("for Success with successful") {
      val v: Validation[String, ResultF] =
        Validation.success(Future.successful(1))
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Success(1)
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
          i shouldBe Success(1)
        }
      }
    }
    describe("for Validation with successful") {
      val v: Validation[String, ResultF] =
        Validation.failure(RemoteService.InternalServerError)
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Failure(RemoteService.InternalServerError)
        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[ToThrowableException] {
          for {
            i <- v.getOrFailed
          } yield {
            i shouldBe 1
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
          s shouldBe Success(1)
        }
      }
      it("for failed") {
        for {
          f <- RemoteService.failed0InternalServerError.transRecover
        } yield {
          f shouldBe Failure(ClientException())
        }
      }
    }
  }
}
