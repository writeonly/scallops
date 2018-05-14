package pl.writeonly.scalaops.ops.mono

import pl.writeonly.scalaops.specs.WhiteAssertSpec

import scala.util.Try

class AnyOpsAssertSpec extends WhiteAssertSpec with AnyOps {
  "A AnyOps" when {
    "for Option" in {
      assert(2.pipeFold(Option(3)) { _ + _ } == 5)
      assert(2.pipeFold(Option(3)) { _ - _ } == -1)
      assert(2.pipeMap(Option(3)) { _ + _ } == 5)
      assert(2.pipeMap(Option(3)) { _ - _ } == -1)
    }
    "for Try" in {
      assert(2.pipeFold(Try(3)) { _ + _ } == 5)
      assert(2.pipeFold(Try(3)) { _ - _ } == -1)
      assert(2.pipeMap(Try(3)) { _ + _ } == 5)
      assert(2.pipeMap(Try(3)) { _ - _ } == -1)
    }
    "for Either" in {
      assert(2.pipeFold(Right(3)) { _ + _ } == 5)
      assert(2.pipeFold(Right(3)) { _ - _ } == -1)
      assert(2.pipeMap(Right(3)) { _ + _ } == 5)
      assert(2.pipeMap(Right(3)) { _ - _ } == -1)
    }
  }

}
