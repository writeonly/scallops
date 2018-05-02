package pl.writeonly.addons.future.library

import org.scalatest.EitherValues
import pl.writeonly.addons.future.RemoteService
import pl.writeonly.addons.future.RemoteService.{CaseException, FutureResult}
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future

class EitherFutureSpec
    extends WhiteFutureSpec
    with EitherValues
    with EitherFuture {
  describe("A Either") {
    describe("for Right with successful") {
      val v = Right[String, FutureResult](Future.successful(1))
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
      val v = Left[String, FutureResult](CaseException().message)
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Left(CaseException().message)
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
          i.left.value shouldBe a[IllegalStateException]
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
          f shouldBe Left(CaseException())
        }
      }
      it("for successful and failed") {
        for {
          s <- RemoteService.successful1.transRecover
          f <- RemoteService.failed0InternalServerError.transRecover
          l = List(s, f)
        } yield {
          s shouldBe Right(1)
          f shouldBe Left(CaseException())
          l shouldBe List(Right(1), Left(CaseException()))
        }
      }

    }
  }

}
