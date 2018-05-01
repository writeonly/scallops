package pl.writeonly.addons.future.scalaz

import pl.writeonly.addons.future.{CaseException, RemoteService}
import pl.writeonly.addons.future.scalaz.HydraFuture._
import pl.writeonly.sons.specs.WhiteFutureSpec
import scalaz.{-\/, \/, \/-}

import scala.concurrent.Future

class HydraFutureSpec extends WhiteFutureSpec {
  describe("A Hydra ") {
    describe("for Right with successful") {
      val v: String \/ Future[Int] = \/-[Future[Int]](Future.successful(1))
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe \/-(1)
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
          i shouldBe \/-(1)
        }
      }
    }
    describe("transRecover") {
      it("for successful") {
        for {
          s <- RemoteService.successful1.transRecover
        } yield {
          s shouldBe \/-(1)
        }
      }
      it("for failed") {
        for {
          f <- RemoteService.failed.transRecover
        } yield {
          f shouldBe -\/(CaseException())
        }
      }
    }
  }
}
