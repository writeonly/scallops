package pl.writeonly.addons.future.api

import pl.writeonly.addons.ops.FutureOps
import pl.writeonly.addons.pipe.Pipe

trait Utils extends Pipe with FutureOps {
  protected def toThrowable(a: Any): Throwable = a match {
    case f: Throwable => f
    case _            => new IllegalStateException(s"$a")
  }

}
