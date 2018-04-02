package pl.writeonly.sons.specs.fixture

import org.scalatest.Matchers
import org.scalatest.concurrent.Eventually
import org.scalatest.fixture

abstract class GrayScalarSpec
    extends fixture.FlatSpec
    with Matchers
    with Eventually
