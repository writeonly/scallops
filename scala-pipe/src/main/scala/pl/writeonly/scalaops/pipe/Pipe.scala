package pl.writeonly.scalaops.pipe

trait Pipe {

  implicit class Pipe[A](a: A) {
    def |>[B](f: A => B) = f(a)

    def ? : Boolean = isNotNull(a)

    def ??(b: => A): A = if (a) a else b

    def $$[B](f: A => B): A = {
      f(a);
      a
    }

    def #!(str: String = ""): A = {
      println(str + a);
      a
    }
  }

  implicit def isNotNull[A](a: A): Boolean = Option(a).isDefined
}

object Pipe extends Pipe
