package pl.writeonly.scalaops.specs

import org.scalatest.{AsyncFunSpec, Matchers}

import scala.concurrent.Future

abstract class WhiteFutureSpec extends AsyncFunSpec with Matchers
