package pl.writeonly.sons.specs.fixture

import org.scalatest.{Matchers, fixture}
import org.scalatest.prop.TableDrivenPropertyChecks

abstract class GrayVectorSpec
    extends fixture.PropSpec
    with TableDrivenPropertyChecks
    with Matchers
