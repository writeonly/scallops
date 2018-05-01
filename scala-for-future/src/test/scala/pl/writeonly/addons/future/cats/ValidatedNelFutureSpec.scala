package pl.writeonly.addons.future.cats

import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyList, ValidatedNel}
import cats.implicits._
import pl.writeonly.addons.future.RemoteService
import pl.writeonly.addons.future.RemoteService.CaseException
import pl.writeonly.addons.future.RemoteTuple.RemoteTuple3
import pl.writeonly.addons.future.cats.ValidatedNelFuture._
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future

class ValidatedNelFutureSpec extends WhiteFutureSpec {
  describe("A ValidatedNel") {
    describe("for Valid with successful") {
      val v: ValidatedNel[String, Future[Int]] =
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
          f <- RemoteService.failed0InternalServerError.transRecover
        } yield {
          f shouldBe Invalid(NonEmptyList(CaseException(), List.empty))
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
            NonEmptyList.one(CaseException(RemoteService.NotImplemented))
          )
          f2 shouldBe Invalid(
            NonEmptyList.one(CaseException(RemoteService.BadGateway))
          )
          p shouldBe Invalid(
            NonEmptyList.of(
              CaseException(RemoteService.NotImplemented),
              CaseException(RemoteService.BadGateway)
            )
          )
        }
      }
    }
  }

}
