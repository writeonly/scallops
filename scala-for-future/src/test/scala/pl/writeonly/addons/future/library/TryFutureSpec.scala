package pl.writeonly.addons.future.library

import pl.writeonly.addons.future.RemoteService
import pl.writeonly.addons.future.RemoteService.{CaseException, FutureResult}
import pl.writeonly.addons.future.library.TryFuture._
import pl.writeonly.sons.specs.WhiteFutureSpec

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class TryFutureSpec extends WhiteFutureSpec {
  describe("A Try") {
    describe("for Success with successful") {
      val v: Try[FutureResult] = Try(Future.successful(1))
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Success(1)
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
          i shouldBe Success(1)
        }
      }
    }
    describe("for Success with failed") {
      val v: Try[FutureResult] = Try(Future.failed(CaseException()))
      it("inSideOut") {
        recoverToSucceededIf[CaseException] {
          for {
            i <- v.inSideOut
          } yield i

        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[CaseException] {
          for {
            i <- v.getOrFailed
          } yield i
        }
      }
      it("transRecover") {
        for {
          i <- v.getOrFailed.transRecover
        } yield {
          i shouldBe Failure(CaseException())
        }
      }
    }
    describe("for Failure") {
      val v: Try[FutureResult] = Failure(CaseException())
      it("inSideOut") {
        for {
          i <- v.inSideOut
        } yield {
          i shouldBe Failure(CaseException())
        }
      }
      it("getOrFailed") {
        recoverToSucceededIf[CaseException] {
          for {
            i <- v.getOrFailed
          } yield i
        }
      }
      it("transRecover") {
        for {
          i <- v.getOrFailed.transRecover
        } yield {
          i shouldBe Failure(CaseException())
        }
      }
    }
    describe("transRecover") {
      it("for successful") {
        for {
          si <- RemoteService.successful1.transRecover
        } yield {
          si shouldBe Success(1)
        }
      }
      it("for failed") {
        for {
          fi <- RemoteService.failed0InternalServerError.transRecover
        } yield {
          fi shouldBe Failure(CaseException())
        }

      }
    }
  }

}
