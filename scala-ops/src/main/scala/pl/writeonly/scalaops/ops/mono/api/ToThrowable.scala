package pl.writeonly.scalaops.ops.mono.api

trait ToThrowable {

  import ToThrowableException._

  protected def toThrowable[A](a: A): Throwable = a match {
    case f: Throwable => f
    case _            => ToThrowable1Exception(a)
  }

  protected def toThrowable[A]: Throwable = ToThrowable0Exception[A]()
}
