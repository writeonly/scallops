package pl.writeonly.scalaops.specs

import org.scalatest.concurrent.Eventually
import org.scalatest.{FlatSpec, Matchers}

abstract class GrayScalarSpec extends FlatSpec with Matchers with Eventually
