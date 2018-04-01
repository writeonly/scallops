package pl.writeonly.sons.utils.ops

object AnyOps {
  implicit def toString[A](a: A): String = a.toString
}
