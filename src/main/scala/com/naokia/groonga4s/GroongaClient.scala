package com.naokia.groonga4s

import java.net.HttpURLConnection

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
      case status if status == HttpURLConnection.HTTP_OK => parser.parse(entity, query)
      case status if status != HttpURLConnection.HTTP_OK =>
        val response = new ErrorResponseParser().parse(entity, query)
        throw new GroongaException(response.returnCode, status, response.message, response.query)
    }
  }
}
