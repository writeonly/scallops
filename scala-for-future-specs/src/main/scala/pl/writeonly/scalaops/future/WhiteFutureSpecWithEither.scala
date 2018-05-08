package pl.writeonly.scalaops.future

import org.scalatest.EitherValues
import pl.writeonly.scalaops.specs.WhiteFutureSpec

abstract class WhiteFutureSpecWithEither
    extends WhiteFutureSpec
    with EitherValues
