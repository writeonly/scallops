package pl.writeonly.scalaops.impl

import pl.writeonly.scalaops.RemoteService
import pl.writeonly.scalaops.RemoteService.{Result, ResultF}
import pl.writeonly.scalaops.api.present.ToThrowableException
import pl.writeonly.scalaops.specs.WhiteFutureSpec

import scala.concurrent.Future

class OptFutureSpec extends WhiteFutureSpec with OptFuture with OptOps {
  describe("A Opt") {

    describe("for Some") {
      val v: Option[Result] = Option[Result](1)
      it("toFuture and getOrFailed") {
        for {
          r <- v.toFuture.transRecover
        } yield {
          r shouldBe v
        }
      }
    }
    describe("for Some with successful") {
      val v: Option[ResultF] = Option(Future.successful(1))
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Some(1)
        }
      }
      it("getOrFailed") {
        for {
          i <- v.getOrFailed
        } yield {
          i shouldBe 1
        }
      }
      it("transRecover") {
        for {
          i <- v.getOrFailed.transRecover
        } yield {
          i shouldBe Some(1)
        }
      }
    }
    describe("for None") {
      val v: Option[ResultF] = Option.empty
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe None
        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[ToThrowableException] {
          for {
            i <- v.getOrFailed
          } yield i

        }
      }
      it("transRecover") {
        for {
          i <- v.getOrFailed.transRecover
        } yield {
          i shouldBe None
        }
      }
    }
    describe("transRecover") {
      it("for successful") {
        for {
          s <- RemoteService.successful1.transRecover
        } yield {
          s shouldBe Some(1)
        }
      }
      it("for failed") {
        for {
          f <- RemoteService.failed0InternalServerError.transRecover
        } yield {
          f shouldBe None
        }
      }
    }

  }

}
