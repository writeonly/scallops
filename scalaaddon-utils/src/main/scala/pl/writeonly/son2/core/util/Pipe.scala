package pl.writeonly.son2.core.util

object Pipe {
  implicit class Pipe[A](a: A) {
    def |>[B](f: A => B) = f(a)
  }
}
