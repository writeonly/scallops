package pl.writeonly.sons.specs.fixture

import org.scalamock.scalatest.MockFactory
import org.scalatest.{PrivateMethodTester, fixture}

abstract class WhiteAssertSpec extends fixture.WordSpec with PrivateMethodTester with MockFactory
