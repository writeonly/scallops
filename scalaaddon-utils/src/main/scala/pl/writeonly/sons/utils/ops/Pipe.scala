package pl.writeonly.sons.utils.ops

object Pipe {
  implicit class Pipe[A](a: A) {
    def |>[B](f: A => B) = f(a)
  }
}
