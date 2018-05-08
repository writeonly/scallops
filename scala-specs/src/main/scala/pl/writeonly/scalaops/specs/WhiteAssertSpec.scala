package pl.writeonly.scalaops.specs

import org.scalamock.scalatest.MockFactory
import org.scalatest.{PrivateMethodTester, WordSpec}

abstract class WhiteAssertSpec
    extends WordSpec
    with PrivateMethodTester
    with MockFactory
