package pl.writeonly.sons.utils.ops

object EitherOps {

  implicit class EitherOps[A, B](either: Either[A, B]) {}

}
