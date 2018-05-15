package pl.writeonly.scalaops.impl

import pl.writeonly.scalaops.api.future.EC
import pl.writeonly.scalaops.api.future.Ops.InSideOut

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
