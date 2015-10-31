package com.naokia.groonga4s

import com.naokia.groonga4s.command._
import com.naokia.groonga4s.response._
import com.naokia.groonga4s.util.request.HttpRequestSender
import scala.util.Try

trait Client{
  def select(parameters: SelectParameters): Try[SelectResponse]
  def load[T](parameters: LoadParameters[T]): Try[LoadResponse]
}

/**
 * A client for groonga.
 *
 * @param uri scheme and host name.
 */
class GroongaClient(uri: String) extends Client {
  val requestSender = new HttpRequestSender(uri)

  /**
   * It requests select command to groonga.
   *
   * @param parameters parameter set
   * @return
   */
  def select(parameters: SelectParameters) = {
    requestSender.send[SelectResponse](new SelectCommand(parameters), new SelectResponseParser)
  }

  def load[T](parameters: LoadParameters[T]): Try[LoadResponse] = {
    requestSender.sendWithBody(new LoadCommand[T](parameters), new LoadResponseParser)
  }
}
