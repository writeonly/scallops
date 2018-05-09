package pl.writeonly.scalaops.future.scalactic

import org.scalactic._
import pl.writeonly.scalaops.future.RemoteService.{ClientException, ResultF}
import pl.writeonly.scalaops.future.RemoteTuple.RemoteTuple3
import pl.writeonly.scalaops.future.{RemoteService, WhiteFutureSpecWithEither}
import pl.writeonly.scalaops.ops.ToThrowableException

import scala.concurrent.Future

class OrEveryFutureSpec extends WhiteFutureSpecWithEither with OrEveryFuture {
  describe("A Or Every") {

    describe("for Good with successful") {
      val v: ResultF Or Every[ErrorMessage] =
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
      val v: ResultF Or Every[ErrorMessage] =
        Bad(One(RemoteService.InternalServerError))
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Bad(One(RemoteService.InternalServerError))
        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[ToThrowableException] {
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
          i.toEither.left.value.head shouldBe a[ToThrowableException]
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
          f shouldBe Bad(One(ClientException()))
        }
      }
      describe("for successful and failed") {
        import Accumulation._
        it("withGood") {
          for {
            s <- RemoteService.successful1.transRecover
            f1 <- RemoteService.failed1NotImplemented.transRecover
            f2 <- RemoteService.failed2BadGateway.transRecover
            p = Accumulation.withGood(s, f1, f2) {
              RemoteTuple3(_, _, _)
            }
          } yield {
            s shouldBe Good(1)
            f1 shouldBe Bad(One(ClientException(RemoteService.NotImplemented)))
            f2 shouldBe Bad(One(ClientException(RemoteService.BadGateway)))
            p shouldBe Bad(
              Many(
                ClientException(RemoteService.NotImplemented),
                ClientException(RemoteService.BadGateway)
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
            f1 shouldBe Bad(One(ClientException(RemoteService.NotImplemented)))
            f2 shouldBe Bad(One(ClientException(RemoteService.BadGateway)))
            p shouldBe Bad(
              Many(
                ClientException(RemoteService.NotImplemented),
                ClientException(RemoteService.BadGateway)
              )
            )
          }
        }
      }
    }
  }

}
