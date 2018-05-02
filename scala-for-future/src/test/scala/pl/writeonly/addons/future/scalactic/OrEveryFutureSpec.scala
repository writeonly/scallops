package pl.writeonly.addons.future.scalactic

import org.scalactic._
import org.scalatest.EitherValues
import pl.writeonly.addons.future.RemoteService
import pl.writeonly.addons.future.RemoteService.{CaseException, FutureResult}
import pl.writeonly.addons.future.RemoteTuple.RemoteTuple3
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future

class OrEveryFutureSpec
    extends WhiteFutureSpec
    with EitherValues
    with OrEveryFuture {
  describe("A Or Every") {

    describe("for Good with successful") {
      val v: FutureResult Or Every[ErrorMessage] =
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
      val v: FutureResult Or Every[ErrorMessage] =
        Bad(One(CaseException().message))
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Bad(One(CaseException().message))
        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[IllegalStateException] {
          for {
            i <- v.getOrFailed
          } yield {
            i
          }
        }
      }
      it("getOrFailed and transRecover") {
        for {
          i <- v.getOrFailed.transRecover
        } yield {
          i.toEither.left.value shouldBe a[One[Throwable]]
          i.toEither.left.value should have size 1
          i.toEither.left.value.head shouldBe a[IllegalStateException]
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
          f shouldBe Bad(One(CaseException()))
        }
      }
      describe("for successful and failed") {
        import Accumulation._
        it("withGood") {
          for {
            s <- RemoteService.successful1.transRecover
            f1 <- RemoteService.failed1NotImplemented.transRecover
            f2 <- RemoteService.failed2BadGateway.transRecover
            p = Accumulation.withGood(s, f1, f2) { RemoteTuple3(_, _, _) }
          } yield {
            s shouldBe Good(1)
            f1 shouldBe Bad(One(CaseException(RemoteService.NotImplemented)))
            f2 shouldBe Bad(One(CaseException(RemoteService.BadGateway)))
            p shouldBe Bad(
              Many(
                CaseException(RemoteService.NotImplemented),
                CaseException(RemoteService.BadGateway)
              )
            )
          }
        }
        it("combined") {
          for {
            s <- RemoteService.successful1.transRecover
            f1 <- RemoteService.failed1NotImplemented.transRecover
            f2 <- RemoteService.failed2BadGateway.transRecover
            p = List(s, f1, f2).combined
          } yield {
            s shouldBe Good(1)
            f1 shouldBe Bad(One(CaseException(RemoteService.NotImplemented)))
            f2 shouldBe Bad(One(CaseException(RemoteService.BadGateway)))
            p shouldBe Bad(
              Many(
                CaseException(RemoteService.NotImplemented),
                CaseException(RemoteService.BadGateway)
              )
            )
          }
        }
      }
    }
  }

}
