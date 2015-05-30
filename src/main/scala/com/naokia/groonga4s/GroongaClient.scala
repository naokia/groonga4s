package com.naokia.groonga4s

import javax.xml.ws.Response

import com.naokia.groonga4s.command.{Command, SelectCommand, SelectParameters}
import com.naokia.groonga4s.response
import com.naokia.groonga4s.response.{ResponseParser, SelectResponseParser, SelectResponse}
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import sun.net.www.http.HttpClient

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
  private def doGetRequest[T <: response.Response](command: Command, parser: ResponseParser[T]): Try[T] = Try {
    val httpClient = HttpClientBuilder.create().build()
    val httpGet = new HttpGet(uri + command.stringify)
    val httpResponse = httpClient.execute(httpGet)
    httpResponse.getStatusLine.getStatusCode match {
      case status if status == 200 => parser.parse(EntityUtils.toString(httpResponse.getEntity, "UTF-8"))
      case status if status != 200 => throw new Exception("statuscode: " + status) //TODO
    }
  }
}
