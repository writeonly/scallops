package pl.writeonly.sons.specs.fixture

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, fixture}

abstract class GrayVectorSpec extends fixture.PropSpec with TableDrivenPropertyChecks with Matchers
