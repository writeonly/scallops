package pl.writeonly.scalaops

import pl.writeonly.scalaops.pipe.Pipe

import scala.concurrent.Future

object RemoteService extends Pipe {
  type Result = Int
  type ResultF = Future[Result]

  val InternalServerError = "Internal Server Error"
  val NotImplemented = "Not Implemented"
  val BadGateway = "Bad Gateway"
  val ServiceUnavailable = "Service Unavailable"
  val GatewayTimeout = "Gateway Timeout"

  def successful1: ResultF = Future.successful(1)

  def failed0InternalServerError: ResultF = InternalServerError |> failed

  def failed(message: String): Future[Nothing] =
    Future.failed(ClientException(message))

  def failed1NotImplemented: ResultF = NotImplemented |> failed

  def failed2BadGateway: ResultF = BadGateway |> failed

  def failed3ServiceUnavailable: ResultF = ServiceUnavailable |> failed

  def failed4GatewayTimeout: ResultF = GatewayTimeout |> failed

  final case class ClientException(message: String = InternalServerError)
      extends RuntimeException(message)

}
