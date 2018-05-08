package pl.writeonly.scalaops.specs.fixture

import org.scalatest.concurrent.Eventually
import org.scalatest.{Matchers, fixture}

abstract class GrayScalarSpec
    extends fixture.FlatSpec
    with Matchers
    with Eventually
