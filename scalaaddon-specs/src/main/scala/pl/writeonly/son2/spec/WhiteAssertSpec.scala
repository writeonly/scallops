package pl.writeonly.son2.spec

import org.scalamock.scalatest.MockFactory
import org.scalatest.{PrivateMethodTester, WordSpec}

abstract class WhiteAssertSpec
    extends WordSpec
    with PrivateMethodTester
    with MockFactory
