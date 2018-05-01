package pl.writeonly.addons.future.library

import cats.data.Validated.{Invalid, Valid}
import pl.writeonly.addons.future.{CaseException, RemoteService}
import pl.writeonly.addons.future.library.EitherFuture._
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future

class EitherFutureSpec extends WhiteFutureSpec {
  describe("A Either") {
    describe("for Right with successful") {
      val v = Right[String, Future[Int]](Future.successful(1))
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
          f <- RemoteService.failed.transRecover
        } yield {
          f shouldBe Left(CaseException())
        }
      }
      it("for successful and failed") {
        for {
          s <- RemoteService.successful1.transRecover
          f <- RemoteService.failed.transRecover
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
