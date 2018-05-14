package pl.writeonly.scalaops.ops

import pl.writeonly.scalaops.ops.AutoCloseableOps.{FAAny, toConsumerAny}
import pl.writeonly.scalaops.specs.WhiteAssertSpec

class AutoCloseableOpsAssertSpec extends WhiteAssertSpec {

  "An AutoCloseableOps" when {
    "s is true" should {
      val s = true
      "pipe return stream.isInstanceOf[StreamerPipeForeach]" in {
        val f: FAAny[String] = (_: String) => {}
        toConsumerAny(f).accept("param")
      }
    }
  }

}
