package pl.writeonly.addons.future

import scala.concurrent.{ExecutionContext, Future}

trait Utils {
  protected def toThrowable(a: Any): Throwable = a match {
    case f: Throwable => f
    case _            => new IllegalStateException(s"$a")
  }

  def recover[S, T](value: Future[T], s: T => S, pf: PartialFunction[Throwable, S])(
    implicit executor: ExecutionContext
  ): Future[S] =
    value
      .transform(s, e => e)
      .recover(pf)
}
