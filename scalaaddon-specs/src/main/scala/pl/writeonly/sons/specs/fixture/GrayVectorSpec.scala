package pl.writeonly.sons.specs.fixture

import org.scalatest.Matchers
import org.scalatest.fixture.PropSpec
import org.scalatest.prop.TableDrivenPropertyChecks

abstract class GrayVectorSpec
    extends PropSpec
    with TableDrivenPropertyChecks
    with Matchers
