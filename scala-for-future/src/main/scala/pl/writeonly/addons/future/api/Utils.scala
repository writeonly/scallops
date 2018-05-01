package pl.writeonly.addons.future.api

import scala.concurrent.{ExecutionContext, Future}

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
