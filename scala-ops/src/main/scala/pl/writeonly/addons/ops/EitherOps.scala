package pl.writeonly.addons.ops

object EitherOps {

  implicit class EitherOps[A, B](either: Either[A, B]) {}

}
