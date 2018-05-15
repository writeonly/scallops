package pl.writeonly.scalaops.monoid.scalactic

import org.scalactic.{Bad, ErrorMessage, Good, Or}
import pl.writeonly.scalaops.RemoteService.{ClientException, ResultF}
import pl.writeonly.scalaops.monoid.api.present.ToThrowableException
import pl.writeonly.scalaops.{RemoteService, WhiteFutureSpecWithEither}

import scala.concurrent.Future

class OrFutureSpec extends WhiteFutureSpecWithEither with OrFuture {
  describe("A Or") {

    describe("for Good with successful") {
      val v: ResultF Or ErrorMessage =
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
      val v: ResultF Or ErrorMessage =
        Bad(RemoteService.InternalServerError)
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Bad(RemoteService.InternalServerError)
        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[ToThrowableException] {
          for {
            i <- v.getOrFailed
          } yield i
        }
      }
      it("getOrFailed and transRecover") {
        for {
          i <- v.getOrFailed.transRecover
        } yield {
          i.toEither.left.value shouldBe a[ToThrowableException]
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
          f shouldBe Bad(ClientException())
        }
      }

    }
  }

}
