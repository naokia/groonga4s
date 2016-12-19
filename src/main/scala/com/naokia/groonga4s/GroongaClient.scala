package com.naokia.groonga4s

import com.naokia.groonga4s.request._
import com.naokia.groonga4s.protocol.HttpRequestSender
import com.naokia.groonga4s.response._

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.{ClassTag, classTag}
import scala.reflect.runtime.universe._


trait Client{
  def select[T: TypeTag: ClassTag](request: SelectRequest)(implicit ec: ExecutionContext): Future[SelectResponse]
  def load[T](request: LoadRequest[T])(implicit ec: ExecutionContext): Future[LoadResponse]
}

/**
 * A client for groonga.
 *
 * @param uri scheme and host name.
 */
class GroongaClient(uri: String) extends Client {
  val requestSender = new HttpRequestSender(uri)

  /**
   * Sends select command to groonga.
   *
   * @param request parameter set
   * @return
   */
  def select[T: TypeTag: ClassTag](request: SelectRequest)(implicit ec: ExecutionContext) = Future{
    val tt = typeTag[T]
    val ct = classTag[T]
    val (entity, requestUri) = requestSender.send(request)
    try {
      new SelectResponse(entity, requestUri)
    } catch {
      case e: NullPointerException => throw ResponseParseException("A Response JSON may be broken, or Version of Groonga is different:" + entity, e)
    }
  }

  /**
   * Sends load command to groonga.
   *
   * @param request
   * @tparam T
   * @return
   */
  def load[T](request: LoadRequest[T])(implicit ec: ExecutionContext): Future[LoadResponse] = Future{
    val (entity, requestUri) = requestSender.sendWithBody(request)
    try {
      new LoadResponse(entity, requestUri)
    } catch {
      case e: NullPointerException => throw ResponseParseException("A Response JSON may be broken, or Version of Groonga is different:" + entity, e)
    }
  }
}
