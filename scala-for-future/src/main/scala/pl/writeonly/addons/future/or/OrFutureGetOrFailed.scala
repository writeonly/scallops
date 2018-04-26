package pl.writeonly.addons.future.or

import org.scalactic.{Bad, Good, Or}
import pl.writeonly.addons.future.ToThrowable
import pl.writeonly.addons.pipe.Pipe._

import scala.concurrent.Future.failed
import scala.concurrent.{ExecutionContext, Future}

object OrFutureGetOrFailed extends ToThrowable {

  type Value[A, B] = Future[A] Or B

  def orFuture[A, B](value: Value[A, B])(implicit ec: ExecutionContext): Future[A] = value match {
    case Good(f: Future[A]) => f
    case Bad(f)             => f |> toThrowable |> failed
  }

  implicit class FutureOr[A, B](value: Value[A, B])(implicit ec: ExecutionContext) {
    def orFuture: Future[A] = OrFutureGetOrFailed.orFuture(value)(ec)
  }

}
