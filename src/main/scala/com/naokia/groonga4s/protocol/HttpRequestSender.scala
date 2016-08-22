package com.naokia.groonga4s.protocol

import java.net.HttpURLConnection

import com.naokia.groonga4s.GroongaException
import com.naokia.groonga4s.command.{Command, CommandWithBody}
import com.naokia.groonga4s.response.{ErrorResponseParser, Response, ResponseParser}
import org.apache.http.client.methods.{HttpGet, HttpPost, HttpRequestBase}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils

import scala.util.Try

/**
 * Adopt this when send request with HTTP
 */
class HttpRequestSender(uri: String) extends RequestSender{
  /**
   * send GET request.
   *
   * @param command
   * @param parser
   * @tparam T
   * @return
   */
  override def send[T, U <: Response](command: Command, parser: ResponseParser[T,U]): Try[U] = doSend(parser) {
    val query = uri + command.getQuery
    new HttpGet(query)
  }

  /**
   * Sends POST request.
   *
   * @param command
   * @param parser
   * @tparam T
   * @return
   */
  override def sendWithBody[T, U <: Response](command: CommandWithBody, parser: ResponseParser[T, U]): Try[U] = doSend(parser) {
    val query = uri + command.getQuery
    val httpPost = new HttpPost(query)
    httpPost.setHeader("Content-Type", "application/json; charset=UTF-8")
    httpPost.setEntity(new StringEntity(command.getBody))

    httpPost
  }

  /**
   * Send HTTP request actually.
   *
   * @param parser
   * @param f
   * @tparam T
   * @return
   */
  private def doSend[T, U <: Response](parser: ResponseParser[T, U])(f: => HttpRequestBase) = Try[U]{
    val httpClient = HttpClientBuilder.create().build()
    val httpMethod = f
    val uri = httpMethod.getURI.toString
    val httpResponse = httpClient.execute(httpMethod)
    try {
      val entity = EntityUtils.toString(httpResponse.getEntity, "UTF-8")
      httpResponse.getStatusLine.getStatusCode match {
        case status if status == HttpURLConnection.HTTP_OK => parser.parse(entity, uri)
        case status if status != HttpURLConnection.HTTP_OK =>
          val response = new ErrorResponseParser().parse(entity, uri)
          throw new GroongaException(response.returnCode, status, response.message, response.query)
      }
    } finally {
      httpClient.close()
    }
  }
}
