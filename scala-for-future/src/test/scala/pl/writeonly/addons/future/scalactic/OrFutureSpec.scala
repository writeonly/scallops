package pl.writeonly.addons.future.scalactic

import org.scalactic.{Bad, ErrorMessage, Good, Or}
import pl.writeonly.addons.future.RemoteService
import pl.writeonly.addons.future.RemoteService.CaseException
import pl.writeonly.addons.future.scalactic.OrFuture._
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future

class OrFutureSpec extends WhiteFutureSpec {
  describe("A Or") {

    describe("for Good with successful") {
      val v: Future[Int] Or ErrorMessage =
        Good(Future.successful(1))
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Good(1)
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
          i shouldBe Good(1)
        }
      }
    }
    describe("transRecover") {
      it("for successful") {
        for {
          s <- RemoteService.successful1.transRecover
        } yield {
          s shouldBe Good(1)
        }
      }
      it("for failed") {
        for {
          f <- RemoteService.failed0InternalServerError.transRecover
        } yield {
          f shouldBe Bad(CaseException())
        }
      }

    }
  }

}
