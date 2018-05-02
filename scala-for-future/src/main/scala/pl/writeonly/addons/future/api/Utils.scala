package pl.writeonly.addons.future.api

import cats.data.NonEmptyList

import scala.concurrent.{ExecutionContext, Future}
import pl.writeonly.addons.pipe.Pipe._

trait Utils {
  protected def toThrowable(a: Any): Throwable = a match {
    case f: Throwable => f
    case _            => new IllegalStateException(s"$a")
  }

  def transform[S, T](
    v: Future[T],
    s: T => S,
    pf: PartialFunction[Throwable, S]
  )(implicit executor: ExecutionContext): Future[S] =
    v.transform(s, e => e)
      .recover(pf)
}
