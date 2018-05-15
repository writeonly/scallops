package pl.writeonly.scalaops

import org.scalatest.EitherValues
import pl.writeonly.scalaops.specs.WhiteFutureSpec

abstract class WhiteFutureSpecWithEither
    extends WhiteFutureSpec
    with EitherValues
