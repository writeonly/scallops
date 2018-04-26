package pl.writeonly.addons.ops

import pl.writeonly.addons.ops.AutoCloseableOps.{FAAny, toConsumerAny}
import pl.writeonly.sons.specs.WhiteAssertSpec

class AutoCloseableOpsAssertSpec extends WhiteAssertSpec {

  "A AutoCloseableOps" when {
    "s is true" should {
      val s = true
      "pipe return stream.isInstanceOf[StreamerPipeForeach]" in {
        val f: FAAny[String] = (_: String) => {}
        toConsumerAny(f).accept("param")
      }
    }
  }

}
