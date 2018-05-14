package pl.writeonly.scalaops.ops.mono.api

trait PipeBothOps[A, M[_]] {
  type F[B] = (A, B) => A
  def pipeFold[B](b: M[B])(f: F[B]): A
  def pipeMap[B](b: M[B])(f: F[B]): A
}
