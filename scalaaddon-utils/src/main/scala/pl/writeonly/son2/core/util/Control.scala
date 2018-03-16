package pl.writeonly.son2.core.util

import java.util.function.Consumer

object Control {

  val UTF_8 = "UTF-8"

  type FAB[A, B] = A => B
  type FAAny[A] = A => Any
  type Closeable = { def close(): Unit }

  def using[A <: Closeable, B](resource: A)(f: FAB[A, B]): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }

  implicit def toConsumerAny[A](f: FAAny[A]): Consumer[A] = new Consumer[A]() {
    override def accept(arg: A): Unit = f(arg)
  }

  implicit class Pipe[A](a: A) {
    def |>[B](f: A => B) = f(a)
  }
}
