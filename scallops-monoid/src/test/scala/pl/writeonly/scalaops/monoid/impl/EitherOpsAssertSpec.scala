package pl.writeonly.scalaops.monoid.impl

import pl.writeonly.scalaops.specs.WhiteAssertSpec

class EitherOpsAssertSpec extends WhiteAssertSpec with EitherOps {
  "A AnyOps" when {
    "for Either" in {
      assert(2.pipeFold(Right(3)) { _ + _ } == 5)
      assert(2.pipeFold(Right(3)) { _ - _ } == -1)
      assert(2.pipeMap(Right(3)) { _ + _ } == 5)
      assert(2.pipeMap(Right(3)) { _ - _ } == -1)
    }
  }

}
