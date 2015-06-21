package com.naokia.groonga4s

import com.naokia.groonga4s.command.{Command, SelectCommand, SelectParameters}
import com.naokia.groonga4s.response._
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils

import scala.util.{Success, Failure, Try}

trait Client{
  def select(parameters: SelectParameters): Try[response.SelectResponse]
}

/**
 * Created by naoki on 15/04/29.
 */
class GroongaClient(uri: String) extends Client {
  def select(parameters: SelectParameters) = doGetRequest[SelectResponse](new SelectCommand(parameters), new SelectResponseParser)

  /**
   *
   * @param command
   * @return
   */
  private def doGetRequest[T <: Response](command: Command, parser: ResponseParser[T]): Try[T] = Try {
    val httpClient = HttpClientBuilder.create().build()
    val query = uri + command.stringify
    val httpGet = new HttpGet(query)
    val httpResponse = httpClient.execute(httpGet)
    val entity = EntityUtils.toString(httpResponse.getEntity, "UTF-8")
    httpResponse.getStatusLine.getStatusCode match {
      case status if status == 200 => parser.parse(entity)
      case status if status != 200 =>
        val response = new ErrorResponseParser().parse(entity)
        throw new GroongaException(response.code, status, response.message, query)
    }
  }
}
