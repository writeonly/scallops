package pl.writeonly.scalaops.ops

object AnyOps {
  implicit def toString[A](a: A): String = a.toString
}
