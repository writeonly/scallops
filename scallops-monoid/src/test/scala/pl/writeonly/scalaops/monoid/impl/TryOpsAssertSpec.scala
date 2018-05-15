package pl.writeonly.scalaops.monoid.impl

import pl.writeonly.scalaops.specs.WhiteAssertSpec

import scala.util.Try

class TryOpsAssertSpec extends WhiteAssertSpec with TryOps {
  "A AnyOps" when {

    "for Try" in {
      assert(2.pipeFold(Try(3)) { _ + _ } == 5)
      assert(2.pipeFold(Try(3)) { _ - _ } == -1)
      assert(2.pipeMap(Try(3)) { _ + _ } == 5)
      assert(2.pipeMap(Try(3)) { _ - _ } == -1)
    }

  }

}
