package pl.writeonly.addons.future.scalaz

import org.scalatest.EitherValues
import pl.writeonly.addons.future.RemoteService
import pl.writeonly.addons.future.RemoteService.{ClientException, ResultF}
import pl.writeonly.addons.ops.ToThrowableException
import pl.writeonly.sons.specs.WhiteFutureSpec
import scalaz.{Failure, NonEmptyList, Success, Validation, ValidationNel}

import scala.concurrent.Future

class ValidationNelFutureSpec
    extends WhiteFutureSpec
    with EitherValues
    with ValidationNelFuture {
  describe("A ValidationNel") {
    describe("for Success with successful") {
      val v: ValidationNel[String, ResultF] =
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
      val v: ValidationNel[String, ResultF] =
        Validation.failureNel(RemoteService.InternalServerError)
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Failure(NonEmptyList(RemoteService.InternalServerError))
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
          i.toEither.left.value shouldBe a[NonEmptyList[Throwable]]
          i.toEither.left.value should have size 1
          i.toEither.left.value.head shouldBe a[ToThrowableException]
          i.toEither.left.value.head.getMessage shouldBe RemoteService.InternalServerError
        }
      }
    }
    describe("for double FailureNel") {
      val v: ValidationNel[String, ResultF] = Failure(
        NonEmptyList(RemoteService.NotImplemented, RemoteService.BadGateway)
      )
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Failure(
            NonEmptyList(RemoteService.NotImplemented, RemoteService.BadGateway)
          )
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
          i.toEither.left.value shouldBe a[NonEmptyList[Throwable]]
          i.toEither.left.value should have size 1
          i.toEither.left.value.head shouldBe a[ToThrowableException]
          i.toEither.left.value.head.getMessage shouldBe NonEmptyList(
            RemoteService.NotImplemented,
            RemoteService.BadGateway
          ).toString
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
          f shouldBe Failure(NonEmptyList(ClientException()))
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
            NonEmptyList(ClientException(RemoteService.NotImplemented))
          )
          f2 shouldBe Failure(
            NonEmptyList(ClientException(RemoteService.BadGateway))
          )
          p shouldBe Failure(
            NonEmptyList(
              ClientException(RemoteService.NotImplemented),
              ClientException(RemoteService.BadGateway)
            )
          )
        }
      }
    }

  }
}
