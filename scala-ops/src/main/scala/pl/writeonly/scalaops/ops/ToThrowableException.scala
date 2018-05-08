package pl.writeonly.scalaops.ops

sealed abstract class ToThrowableException(massage: String)
    extends IllegalStateException(massage)

object ToThrowableException {

  final case class ToThrowable1Exception[V](value: V)
      extends ToThrowableException(s"$value")

  final case class ToThrowable0Exception[V]() extends ToThrowableException("")
}
