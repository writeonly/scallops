package pl.writeonly.addons.ops

object AnyOps {
  implicit def toString[A](a: A): String = a.toString
}
