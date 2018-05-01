package pl.writeonly.addons.future

import scala.concurrent.Future

import pl.writeonly.addons.pipe.Pipe._

object RemoteService {
  type FutureResult = Future[Int]

  val InternalServerError = "Internal Server Error"
  val NotImplemented = "Not Implemented"
  val BadGateway = "Bad Gateway"
  val ServiceUnavailable = "Service Unavailable"
  val GatewayTimeout = "Gateway Timeout"

  def successful1: FutureResult = Future.successful(1)
  def failed0InternalServerError: FutureResult = InternalServerError |> failed
  def failed1NotImplemented: FutureResult = NotImplemented |> failed
  def failed2BadGateway: FutureResult = BadGateway |> failed
  def failed3ServiceUnavailable: FutureResult = ServiceUnavailable |> failed
  def failed4GatewayTimeout: FutureResult = GatewayTimeout |> failed

  def failed(message: String) = Future.failed(CaseException(message))

  final case class CaseException(message: String = InternalServerError)
      extends Exception(message)

}
