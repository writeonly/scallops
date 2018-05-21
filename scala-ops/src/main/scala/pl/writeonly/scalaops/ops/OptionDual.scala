package pl.writeonly.scalaops.ops

case class OptionDual[A](prev: Option[A], next: Option[A]) {
  def map[B](f: A => B): OptionDual[B] = OptionDual(prev.map(f), next.map(f))
}
