package pl.writeonly.sons.specs.fixture

import org.scalatest.Matchers
import org.scalatest.concurrent.Eventually
import org.scalatest.fixture.FlatSpec

abstract class GrayScalarSpec extends FlatSpec with Matchers with Eventually
