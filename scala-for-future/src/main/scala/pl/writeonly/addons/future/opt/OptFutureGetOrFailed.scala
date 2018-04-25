package pl.writeonly.addons.future.opt

import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future.failed
import scala.concurrent.{ExecutionContext, Future}

object OptFutureGetOrFailed {
  def optFuture[A](
    value: Option[Future[A]]
  )(implicit ec: ExecutionContext): Future[A] = value match {
    case Some(f: Future[A]) => f
    case None               => new IllegalStateException() |> failed
  }

  implicit class FutureOpt[A](opt: Option[Future[A]])(
    implicit ec: ExecutionContext
  ) {
    def optFuture: Future[A] = OptFutureGetOrFailed.optFuture(opt)(ec)
  }

}
