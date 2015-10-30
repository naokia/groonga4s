package com.naokia.groonga4s

import java.net.HttpURLConnection
import com.naokia.groonga4s.command._
import com.naokia.groonga4s.response._
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.{HttpPost, HttpRequestBase, HttpGet}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.protocol.HTTP
import org.apache.http.util.EntityUtils
import scala.util.{Failure, Success, Try}

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

  //TODO write LoadCommandParser
  def load[T](parameters: LoadParameters[T]): Try[response.SelectResponse] = doPostRequest(new LoadCommand[T](parameters), new SelectResponseParser)

  private def doGetRequest[T <: Response](command: Command, parser: ResponseParser[T]): Try[T] = doHttpRequest(parser) {
    val query = uri + command.getQuery
    new HttpGet(query)
  }

  private def doPostRequest[T <: Response](command: PostCommand, parser: ResponseParser[T]): Try[T] = doHttpRequest(parser) {
    val query = uri + command.getQuery
    val httpPost = new HttpPost(query)
    httpPost.setHeader("Content-Type", "application/json; charset=UTF-8")
    httpPost.setEntity(new StringEntity(command.getBody))

    httpPost
  }

  private def doHttpRequest[T <: Response](parser: ResponseParser[T])(f: => HttpRequestBase) = Try{
    val httpClient = HttpClientBuilder.create().build()
    val httpMethod = f
    val uri = httpMethod.getURI.toString
    val httpResponse = httpClient.execute(httpMethod)
    val entity = EntityUtils.toString(httpResponse.getEntity, "UTF-8")
    httpResponse.getStatusLine.getStatusCode match {
      case status if status == HttpURLConnection.HTTP_OK => parser.parse(entity, uri)
      case status if status != HttpURLConnection.HTTP_OK =>
        val response = new ErrorResponseParser().parse(entity, uri)
        throw new GroongaException(response.returnCode, status, response.message, response.query)
    }
  }
}
