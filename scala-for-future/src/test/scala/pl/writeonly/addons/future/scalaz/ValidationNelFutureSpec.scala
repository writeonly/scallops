package pl.writeonly.addons.future.scalaz

import org.scalatest.EitherValues
import pl.writeonly.addons.future.RemoteService
import pl.writeonly.addons.future.RemoteService.{CaseException, FutureResult}
import pl.writeonly.sons.specs.WhiteFutureSpec
import scalaz.{Failure, NonEmptyList, Success, Validation, ValidationNel}

import scala.concurrent.Future

class ValidationNelFutureSpec
    extends WhiteFutureSpec
    with EitherValues
    with ValidationNelFuture {
  describe("A ValidationNel") {
    describe("for Success with successful") {
      val v: ValidationNel[String, FutureResult] =
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
    describe("for FailureNel") {
      val v: ValidationNel[String, FutureResult] =
        Validation.failureNel(CaseException().message)
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Failure(NonEmptyList(CaseException().message))
        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[IllegalStateException] {
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
          i.toEither.left.value shouldBe a[NonEmptyList[Throwable]]
          i.toEither.left.value should have size 1
          i.toEither.left.value.head shouldBe a[IllegalStateException]
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