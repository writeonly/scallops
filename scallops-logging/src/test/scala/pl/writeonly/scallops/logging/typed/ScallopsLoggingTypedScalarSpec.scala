package pl.writeonly.scallops.logging.typed

import akka.actor.ActorSystem
import akka.event.Logging.MDC
import pl.writeonly.scalaops.specs.GrayScalarSpec

class ScallopsLogginTypedScalarSpec extends GrayScalarSpec {
  it should "log message use logger" in {
    implicit val mdc: MDC = Map("key" -> "value")

    val logging = new ScallopsLoggingTyped {
      override implicit protected val actorSystem: ActorSystem = ActorSystem()

      def logMessage(): Unit = {
        logger.error(new IllegalStateException(), "it is error !")
        logger.error(
          new IllegalStateException(),
          "it is error with {} !",
          "arg"
        )
        logger.error("it is error !")
        logger.error("it is error with {} !", "arg")
        logger.warning("it is warning !")
        logger.warning("it is warning with {} !", "arg")
        logger.info("it is info !")
        logger.info("it is info with {} !", "arg")
        logger.debug("it is debug !")
        logger.debug("it is debug with {} !", "arg")
      }
    }
    logging.logMessage()

    logging.logger.isErrorEnabled shouldBe true
    logging.logger.isWarningEnabled shouldBe true
    logging.logger.isInfoEnabled shouldBe true
    logging.logger.isDebugEnabled shouldBe false
  }
}
