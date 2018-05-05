package pl.writeonly.addons.future

import org.scalatest.EitherValues
import pl.writeonly.sons.specs.WhiteFutureSpec

abstract class WhiteFutureSpecWithEither
    extends WhiteFutureSpec
    with EitherValues
