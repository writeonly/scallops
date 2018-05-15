package pl.writeonly.scalaops.monoid.scalaz

import pl.writeonly.scalaops.RemoteService.{ClientException, ResultF}
import pl.writeonly.scalaops.monoid.api.present.ToThrowableException
import pl.writeonly.scalaops.{RemoteService, WhiteFutureSpecWithEither}
import scalaz.{-\/, \/, \/-}

import scala.concurrent.Future

class HydraFutureSpec extends WhiteFutureSpecWithEither with HydraFuture {
  describe("A Hydra ") {
    describe("for Right with successful") {
      val v: String \/ ResultF = \/-[ResultF](Future.successful(1))
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
    describe("for Left") {
      val v: String \/ ResultF = -\/(RemoteService.InternalServerError)
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe -\/(RemoteService.InternalServerError)
        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[ToThrowableException] {
          for {
            i <- v.getOrFailed
          } yield {
            i shouldBe 1
          }

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
          s shouldBe \/-(1)
        }
      }
      it("for failed") {
        for {
          f <- RemoteService.failed0InternalServerError.transRecover
        } yield {
          f shouldBe -\/(ClientException())
        }
      }
    }
  }
}
