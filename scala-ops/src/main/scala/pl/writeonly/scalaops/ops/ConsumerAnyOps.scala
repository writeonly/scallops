package pl.writeonly.scalaops.ops

import java.util.function.Consumer

trait ConsumerAnyOps {

  type ConsumerAny[A] = A => Any

  implicit class FunctionToAnyOps[A](f: ConsumerAny[A]) {
    def toConsumerAny: Consumer[A] = new Consumer[A]() {
      override def accept(arg: A): Unit = f(arg)
    }

  }

}
