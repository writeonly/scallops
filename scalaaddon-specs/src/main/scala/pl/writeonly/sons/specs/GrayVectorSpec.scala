package pl.writeonly.sons.specs

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}

abstract class GrayVectorSpec extends PropSpec with TableDrivenPropertyChecks with Matchers
