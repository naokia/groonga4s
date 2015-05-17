package com.naokia.groonga4s

import javax.xml.ws.Response

import com.naokia.groonga4s.command.{Command, SelectCommand, SelectParameters}
import com.naokia.groonga4s.response
import com.naokia.groonga4s.response.SelectResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import sun.net.www.http.HttpClient

import scala.util.{Failure, Try}

trait Client{
  def select[T](parameters: SelectParameters): Try[response.SelectResponse[T]]
}

/**
 * Created by naoki on 15/04/29.
 */
class GroongaClient(uri: String) extends Client{
  def select[T](parameters: SelectParameters) = doGetRequest[T](new SelectCommand(parameters))

  /**
   *
   * @param command
   * @return
   */
  private def doGetRequest[T](command: Command):Try[response.SelectResponse[T]] = Try{
    val httpClient = HttpClientBuilder.create().build()
    val httpGet = new HttpGet(uri + command.stringify)
    val httpResponse = httpClient.execute(httpGet)
    httpResponse.getStatusLine.getStatusCode match {
      case status if status == 200 => SelectResponse(0,0.0,0.0, 0, Seq[Map[String, Any]]()) //TODO
      case status if status != 200 => throw new Exception("statuscode: " + status) //TODO 適切なExceptionを。
    }
  }
}
