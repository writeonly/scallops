package pl.writeonly.sons.specs.fixture

import org.scalamock.scalatest.MockFactory
import org.scalatest.PrivateMethodTester
import org.scalatest.fixture.WordSpec

abstract class WhiteAssertSpec
    extends WordSpec
    with PrivateMethodTester
    with MockFactory
