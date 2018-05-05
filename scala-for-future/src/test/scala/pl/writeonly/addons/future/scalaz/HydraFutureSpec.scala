package pl.writeonly.addons.future.scalaz

import org.scalatest.EitherValues
import pl.writeonly.addons.future.RemoteService
import pl.writeonly.addons.future.RemoteService.{ClientException, ResultF}
import pl.writeonly.addons.ops.ToThrowableException
import pl.writeonly.sons.specs.WhiteFutureSpec
import scalaz.{-\/, \/, \/-}

import scala.concurrent.Future

class HydraFutureSpec
    extends WhiteFutureSpec
    with EitherValues
    with HydraFuture {
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
