package pl.writeonly.addons.future.scalactic

import org.scalactic.{Bad, ErrorMessage, Good, Or}
import org.scalatest.EitherValues
import pl.writeonly.addons.future.RemoteService
import pl.writeonly.addons.future.RemoteService.{CaseException, FutureResult}
import pl.writeonly.addons.future.scalactic.OrFuture._
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future

class OrFutureSpec extends WhiteFutureSpec with EitherValues {
  describe("A Or") {

    describe("for Good with successful") {
      val v: FutureResult Or ErrorMessage =
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
    describe("for Bad") {
      val v: FutureResult Or ErrorMessage =
        Bad(CaseException().message)
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Bad(CaseException().message)
        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[IllegalStateException] {
          for {
            i <- v.getOrFailed
          } yield i
        }
      }
      it("getOrFailed and transRecover") {
        for {
          i <- v.getOrFailed.transRecover
        } yield {
          i.toEither.left.value shouldBe a[IllegalStateException]
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
