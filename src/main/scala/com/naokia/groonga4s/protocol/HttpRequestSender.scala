package com.naokia.groonga4s.protocol

import java.net.HttpURLConnection

import com.naokia.groonga4s.{Entity, GroongaException, RequestUri}
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
   * @return
   */
  override def send(command: Command): (Entity, RequestUri) ={
    val requestUri = uri + command.getQuery
    (doSend(new HttpGet(requestUri)), requestUri)
  }

  /**
   * Sends POST request.
   *
   * @param command
   * @return
   */
  override def sendWithBody(command: CommandWithBody): (Entity, RequestUri) = {
    val requestUri = uri + command.getQuery
    val httpPost = new HttpPost(requestUri)
    httpPost.setHeader("Content-Type", "application/json; charset=UTF-8")
    httpPost.setEntity(new StringEntity(command.getBody))
    (doSend(httpPost), requestUri)
  }

  /**
   * Send HTTP request actually.
   *
   * @tparam T
   * @return
   */
  private def doSend[T, U <: Response](httpMethod: HttpRequestBase): Entity = {
    val httpClient = HttpClientBuilder.create().build()
    val uri = httpMethod.getURI.toString
    val httpResponse = httpClient.execute(httpMethod)
    try {
      val entity = EntityUtils.toString(httpResponse.getEntity, "UTF-8")
      httpResponse.getStatusLine.getStatusCode match {
        case status if status == HttpURLConnection.HTTP_OK => entity
        case status if status != HttpURLConnection.HTTP_OK =>
          val response = new ErrorResponseParser().parse(entity, uri)
          throw new GroongaException(response.returnCode, status, response.message, response.query)
      }
    } finally {
      httpResponse.close()
      httpClient.close()
    }
  }
}
