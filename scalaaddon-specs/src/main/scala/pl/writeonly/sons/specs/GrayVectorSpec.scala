package pl.writeonly.sons.specs

import org.scalatest.{Matchers, PropSpec}
import org.scalatest.prop.TableDrivenPropertyChecks

abstract class GrayVectorSpec
    extends PropSpec
    with TableDrivenPropertyChecks
    with Matchers
