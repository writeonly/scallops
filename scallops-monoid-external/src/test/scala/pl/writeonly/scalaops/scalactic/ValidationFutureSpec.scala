package pl.writeonly.scalaops.scalactic

import org.scalactic.{Fail, Pass, Validation}
import pl.writeonly.scalaops.RemoteService.ClientException
import pl.writeonly.scalaops.monoid.api.present.ToThrowableException
import pl.writeonly.scalaops.{RemoteService, WhiteFutureSpecWithEither}

import scala.runtime.BoxedUnit

class ValidationFutureSpec
    extends WhiteFutureSpecWithEither
    with ValidationFuture {

  case object Done

  describe("A Validation") {
    describe("for Pass") {
      val v: Validation[String] = Pass
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe null
        }
      }
      it("unitOrFailed") {
        for {
          i <- v.unitOrFailed
        } yield {
          i shouldBe a[BoxedUnit]
          i shouldBe ()
        }
      }
      it("unitOrFailed and transRecover") {
        for {
          i <- v.unitOrFailed.transRecover
        } yield {
          i shouldBe Pass
        }

      }
      it("getOrFailed") {
        for {
          i <- v.getOrFailed(Done)
        } yield {
          i shouldBe Done
        }
      }
      it("getOrFailed and transRecover") {
        for {
          i <- v.getOrFailed(Done).transRecover
        } yield {
          i shouldBe Pass
        }
      }
    }
    describe("for Fail") {
      val v: Validation[String] = Fail(RemoteService.InternalServerError)
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Fail(RemoteService.InternalServerError)
        }
      }
      it("unitOrFailed") {
        recoverToSucceededIf[ToThrowableException] {
          for {
            i <- v.unitOrFailed
          } yield {
            i shouldBe RemoteService.InternalServerError
          }
        }
      }
      it("unitOrFailed and transRecover") {
        for {
          i <- v.unitOrFailed.transRecover
        } yield {
          i shouldBe a[Fail[ToThrowableException]]
        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[ToThrowableException] {
          for {
            i <- v.getOrFailed(Done)
          } yield {
            i shouldBe RemoteService.InternalServerError
          }
        }
      }
      it("getOrFailed and transRecover") {
        for {
          i <- v.getOrFailed(Done).transRecover
        } yield {
          i shouldBe a[Fail[ToThrowableException]]
        }
      }
    }
    describe("transRecover") {
      it("for successful") {
        for {
          s <- RemoteService.successful1.transRecover
        } yield {
          s shouldBe Pass
        }
      }
      it("for failed") {
        for {
          f <- RemoteService.failed0InternalServerError.transRecover
        } yield {
          f shouldBe Fail(ClientException())
        }
      }
      it("for successful and failed") {
        for {
          s <- RemoteService.successful1.transRecover
          f <- RemoteService.failed0InternalServerError.transRecover
          l = List(s, f)
        } yield {
          s shouldBe Pass
          f shouldBe Fail(ClientException())
          l shouldBe List(Pass, Fail(ClientException()))
        }
      }

    }
  }

}
