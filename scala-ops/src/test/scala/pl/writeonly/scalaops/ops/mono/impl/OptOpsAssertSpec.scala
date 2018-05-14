package pl.writeonly.scalaops.ops.mono.impl

import pl.writeonly.scalaops.specs.WhiteAssertSpec

class OptOpsAssertSpec extends WhiteAssertSpec with OptOps {
  "A AnyOps" when {
    "for Option" in {
      assert(2.pipeFold(Option(3)) { _ + _ } == 5)
      assert(2.pipeFold(Option(3)) { _ - _ } == -1)
      assert(2.pipeMap(Option(3)) { _ + _ } == 5)
      assert(2.pipeMap(Option(3)) { _ - _ } == -1)
    }

  }

}
