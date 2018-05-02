package pl.writeonly.addons.future.scalaz

import pl.writeonly.addons.future.RemoteService
import pl.writeonly.addons.future.RemoteService.{CaseException, FutureResult}
import pl.writeonly.addons.future.scalaz.ValidationNelFuture._
import pl.writeonly.sons.specs.WhiteFutureSpec
import scalaz.{Failure, NonEmptyList, Success, ValidationNel}

import scala.concurrent.Future

class ValidationNelFutureSpec extends WhiteFutureSpec {
  describe("A ValidationNel") {
    describe("for Validation with successful") {
      val v: ValidationNel[String, FutureResult] =
        Success(Future.successful(1))
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
          f shouldBe Failure(NonEmptyList(CaseException()))
        }
      }
      it("for successful and failed") {
        import scalaz.syntax.apply._
        for {
          s <- RemoteService.successful1.transRecover
          f1 <- RemoteService.failed1NotImplemented.transRecover
          f2 <- RemoteService.failed2BadGateway.transRecover
          p = (s |@| f1 |@| f2) { _ + _ + _ }
        } yield {
          s shouldBe Success(1)
          f1 shouldBe Failure(
            NonEmptyList(CaseException(RemoteService.NotImplemented))
          )
          f2 shouldBe Failure(
            NonEmptyList(CaseException(RemoteService.BadGateway))
          )
          p shouldBe Failure(
            NonEmptyList(
              CaseException(RemoteService.NotImplemented),
              CaseException(RemoteService.BadGateway)
            )
          )
        }
      }
    }

  }
}
