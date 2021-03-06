package pl.writeonly.scalaops.ops

import java.util.function.Consumer

import pl.writeonly.scalaops.ops.AutoCloseableOps.FAB

trait AutoCloseableOps {
  implicit class AutoCloseableOps[A <: AutoCloseable](resource: A) {
    def using[B](f: FAB[A, B]): B =
      try {
        f(resource)
      } finally {
        resource.close()
      }
  }
}

object AutoCloseableOps {

  type FAB[A, B] = A => B
  type FAAny[A] = A => Any
  val UTF_8 = "UTF-8"

  def using[A <: AutoCloseable, B](resource: A)(f: FAB[A, B]): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }

  implicit def toConsumerAny[A](f: FAAny[A]): Consumer[A] = new Consumer[A]() {
    override def accept(arg: A): Unit = f(arg)
  }

}
