package pl.writeonly.addons.ops

import pl.writeonly.sons.specs.WhiteAssertSpec
import AutoCloseableOps.{FAAny, toConsumerAny}

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
