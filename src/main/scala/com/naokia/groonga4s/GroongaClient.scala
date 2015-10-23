package com.naokia.groonga4s

import java.net.HttpURLConnection
import com.naokia.groonga4s.command.{LoadParameters, Command, SelectCommand, SelectParameters}
import com.naokia.groonga4s.response._
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import scala.util.{Success, Try}

trait Client{
  def select(parameters: SelectParameters): Try[response.SelectResponse]
  def load[T](parameters: LoadParameters[T]): Try[response.SelectResponse]
}

/**
 * A client for groonga.
 *
 * @param uri scheme and host name.
 */
class GroongaClient(uri: String) extends Client {
  /**
   * It requests select command to groonga.
   *
   * @param parameters parameter set
   * @return
   */
  def select(parameters: SelectParameters) = doGetRequest[SelectResponse](new SelectCommand(parameters), new SelectResponseParser)

  def load[T](parameters: LoadParameters[T]): Try[response.SelectResponse] = {
    throw new Exception
  }

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
