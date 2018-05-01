package pl.writeonly.addons.future

import scala.concurrent.Future

import pl.writeonly.addons.pipe.Pipe._

object RemoteService {
  type FutureResult = Future[Int]

  val NotImplemented = "Not Implemented"
  val BadGateway = "Bad Gateway"
  val ServiceUnavailable = "Service Unavailable"
  val GatewayTimeout = "Gateway Timeout"

  def successful1: FutureResult = Future.successful(1)
  def failed: FutureResult = Future.failed(CaseException())
  def failed1NotImplemented: FutureResult = NotImplemented |> apply
  def failed2BadGateway: FutureResult = BadGateway |> apply
  def failed3ServiceUnavailable: FutureResult = ServiceUnavailable |> apply
  def failed4GatewayTimeout: FutureResult = GatewayTimeout |> apply

  def apply(message: String) = Future.failed(CaseException(message))
}
