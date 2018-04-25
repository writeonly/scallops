package pl.writeonly.addons.ops

import java.util.function.Consumer

object AutoCloseableOps {

  val UTF_8 = "UTF-8"
  type FAB[A, B] = A => B
  type FAAny[A] = A => Any

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
