package pl.writeonly.scalaops.future.library

import pl.writeonly.scalaops.future.RemoteService.{
  ClientException,
  Result,
  ResultF
}
import pl.writeonly.scalaops.future.{RemoteService, WhiteFutureSpecWithEither}
import pl.writeonly.scalaops.ops.ToThrowableException

import scala.concurrent.Future

class EitherFutureSpec extends WhiteFutureSpecWithEither with EitherFuture {
  describe("A Either") {

    describe("for Right") {
      val v: Either[String, Int] = Right[String, Result](1)
      it("toFuture and getOrFailed") {
        for {
          r <- v.toFuture.transRecover
        } yield {
          r shouldBe v
        }
      }
    }

    describe("for Right with successful") {
      val v = Right[String, ResultF](Future.successful(1))
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Right(1)
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
          i shouldBe Right(1)
        }
      }
    }
    describe("for Left with successful") {
      val v = Left[String, ResultF](RemoteService.InternalServerError)
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Left(RemoteService.InternalServerError)
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
          i.left.value shouldBe a[ToThrowableException]
        }
      }
    }
    describe("transRecover") {
      it("for successful") {
        for {
          s <- RemoteService.successful1.transRecover
        } yield {
          s shouldBe Right(1)
        }
      }
      it("for failed") {
        for {
          f <- RemoteService.failed0InternalServerError.transRecover
        } yield {
          f shouldBe Left(ClientException())
        }
      }
      it("for successful and failed") {
        for {
          s <- RemoteService.successful1.transRecover
          f <- RemoteService.failed0InternalServerError.transRecover
          l = List(s, f)
        } yield {
          s shouldBe Right(1)
          f shouldBe Left(ClientException())
          l shouldBe List(Right(1), Left(ClientException()))
        }
      }

    }
  }

}