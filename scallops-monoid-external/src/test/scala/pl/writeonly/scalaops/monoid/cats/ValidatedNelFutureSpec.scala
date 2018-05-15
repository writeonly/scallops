package pl.writeonly.scalaops.monoid.cats

import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.implicits._
import pl.writeonly.scalaops.RemoteService.{ClientException, ResultF}
import pl.writeonly.scalaops.RemoteTuple.RemoteTuple3
import pl.writeonly.scalaops.monoid.api.present.ToThrowableException
import pl.writeonly.scalaops.{RemoteService, WhiteFutureSpecWithEither}

import scala.concurrent.Future

class ValidatedNelFutureSpec
    extends WhiteFutureSpecWithEither
    with ValidatedNelFuture {
  describe("A ValidatedNel") {
    describe("for Valid with successful") {
      val v: ValidatedNel[String, ResultF] =
        Validated.validNel(Future.successful(1))
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
    describe("for Invalid ") {
      val v: ValidatedNel[String, ResultF] =
        Validated.invalidNel(RemoteService.InternalServerError)
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Invalid(
            NonEmptyList.one(RemoteService.InternalServerError)
          )
        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[ToThrowableException] {
          for {
            i <- v.getOrFailed
          } yield {
            i shouldBe NonEmptyList.one(RemoteService.InternalServerError)
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
    describe("for double Invalid ") {
      val v: ValidatedNel[String, ResultF] = Validated.Invalid(
        NonEmptyList.of(RemoteService.NotImplemented, RemoteService.BadGateway)
      )
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Invalid(
            NonEmptyList
              .of(RemoteService.NotImplemented, RemoteService.BadGateway)
          )
        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[ToThrowableException] {
          for {
            i <- v.getOrFailed
          } yield {
            i shouldBe NonEmptyList.of(
              RemoteService.NotImplemented,
              RemoteService.BadGateway
            )
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
          i.toEither.left.value.head.getMessage shouldBe NonEmptyList
            .of(RemoteService.NotImplemented, RemoteService.BadGateway)
            .toString
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
          f shouldBe Invalid(NonEmptyList.one(ClientException()))
        }
      }
      it("for successful and failed") {
        for {
          s <- RemoteService.successful1.transRecover
          f1 <- RemoteService.failed1NotImplemented.transRecover
          f2 <- RemoteService.failed2BadGateway.transRecover
          p = (s, f1, f2).mapN(RemoteTuple3[Int])
        } yield {
          s shouldBe Valid(1)
          f1 shouldBe Invalid(
            NonEmptyList.one(ClientException(RemoteService.NotImplemented))
          )
          f2 shouldBe Invalid(
            NonEmptyList.one(ClientException(RemoteService.BadGateway))
          )
          p shouldBe Invalid(
            NonEmptyList.of(
              ClientException(RemoteService.NotImplemented),
              ClientException(RemoteService.BadGateway)
            )
          )
        }
      }
    }
  }

}
