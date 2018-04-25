package pl.writeonly.addons.ops

object IterableOps {
  implicit def toInt[A <: Seq[B], B](seq: A): Int = seq.size
}
