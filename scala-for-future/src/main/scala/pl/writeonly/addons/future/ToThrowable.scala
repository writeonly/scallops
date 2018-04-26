package pl.writeonly.addons.future

trait ToThrowable {
  protected def toThrowable(a: Any): Throwable = a match {
    case f: Throwable => f
    case _ => new IllegalStateException(s"$a")
  }

}
