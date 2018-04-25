package pl.writeonly.addons.future.opt

import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future.successful
import scala.concurrent.{ExecutionContext, Future}

object OptFutureInSideOut {
  def optFuture[A](
    value: Option[Future[A]]
  )(implicit ec: ExecutionContext): Future[Option[A]] = value match {
    case Some(f: Future[A]) => for (a <- f) yield Option(a)
    case None               => None |> successful
  }

  implicit class FutureOpt[A](opt: Option[Future[A]])(
    implicit ec: ExecutionContext
  ) {
    def optFuture: Future[Option[A]] = OptFutureInSideOut.optFuture(opt)(ec)
  }

}
