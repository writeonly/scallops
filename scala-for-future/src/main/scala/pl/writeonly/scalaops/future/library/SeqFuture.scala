package pl.writeonly.scalaops.future.library

import pl.writeonly.scalaops.future.api.EC
import pl.writeonly.scalaops.future.api.Ops.InSideOut

import scala.concurrent.Future

object SeqFuture {
  def inSideOut[A](v: Seq[Future[A]])(implicit ec: EC): Future[Seq[A]] =
    Future.sequence(v)

  implicit class SeqFutureInSideOut[A](v: Seq[Future[A]])
      extends InSideOut[Seq[A]] {
    override def inSideOut(implicit ec: EC): Future[Seq[A]] =
      SeqFuture.inSideOut(v)(ec)
  }

}
