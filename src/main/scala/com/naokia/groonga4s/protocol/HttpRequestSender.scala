package com.naokia.groonga4s.protocol

import java.net.HttpURLConnection

import com.naokia.groonga4s.{Entity, GroongaException, RequestUri}
import com.naokia.groonga4s.request.{Request, RequestWithBody}
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
   * @param request
   * @return
   */
  override def send(request: Request): (Entity, RequestUri) ={
    val requestUri = uri + request.toQuery
    val httpGet = new HttpGet(requestUri)
    try {
      (doSend(httpGet), requestUri)
    } finally {
      httpGet.releaseConnection()
    }
  }

  /**
   * Sends POST request.
   *
   * @param request
   * @return
   */
  override def sendWithBody(request: RequestWithBody): (Entity, RequestUri) = {
    val requestUri = uri + request.toQuery
    val httpPost = new HttpPost(requestUri)
    try {
      httpPost.setHeader("Content-Type", "application/json; charset=UTF-8")
      httpPost.setEntity(new StringEntity(request.getBody))
      (doSend(httpPost), requestUri)
    } finally {
      httpPost.releaseConnection()
    }
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
    val entity = EntityUtils.toString(httpResponse.getEntity, "UTF-8")
    httpResponse.getStatusLine.getStatusCode match {
      case status if status == HttpURLConnection.HTTP_OK => entity
      case status if status != HttpURLConnection.HTTP_OK =>
        val response = new ErrorResponseParser().parse(entity, uri)
        throw GroongaException(response.returnCode, status, response.message, response.query)
    }
  }
}
