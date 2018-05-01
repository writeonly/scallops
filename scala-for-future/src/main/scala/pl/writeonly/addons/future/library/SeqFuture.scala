package pl.writeonly.addons.future.library

import pl.writeonly.addons.future.api.EC
import pl.writeonly.addons.future.api.Ops.InSideOut

import scala.concurrent.Future

object SeqFuture {
  def inSideOut[A](v: Seq[Future[A]])(implicit ec: EC): Future[Seq[A]] =
    Future
      .successful(v)
      .flatMap(Future.sequence(_))

  implicit class SeqFutureInSideOut[A](v: Seq[Future[A]])
      extends InSideOut[Seq[A]] {
    override def inSideOut(implicit ec: EC): Future[Seq[A]] =
      SeqFuture.inSideOut(v)(ec)
  }
}
