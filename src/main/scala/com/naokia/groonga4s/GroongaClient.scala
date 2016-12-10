package com.naokia.groonga4s

import com.naokia.groonga4s.command._
import com.naokia.groonga4s.protocol.HttpRequestSender
import com.naokia.groonga4s.response._

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.{ClassTag, classTag}
import scala.reflect.runtime.universe._

trait Client{
  def select[T: TypeTag: ClassTag](parameters: SelectParameters[T])(implicit ec: ExecutionContext): Future[SelectResponse]
  def load[T](parameters: LoadParameters[T])(implicit ec: ExecutionContext): Future[LoadResponse]
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
   * @param parameters parameter set
   * @return
   */
  def select[T: TypeTag: ClassTag](parameters: SelectParameters[T])(implicit ec: ExecutionContext) = Future{
    val tt = typeTag[T]
    val ct = classTag[T]
    val (entity, requestUri) = requestSender.send(new SelectCommand(parameters))
    try {
      new SelectResponse(entity, requestUri)
    } catch {
      case e: NullPointerException => throw new ResponseParseException("A Response JSON may be broken, or Version of Groonga is different:" + entity, e)
    }
  }

  /**
   * Sends load command to groonga.
   *
   * @param parameters
   * @tparam T
   * @return
   */
  def load[T](parameters: LoadParameters[T])(implicit ec: ExecutionContext): Future[LoadResponse] = Future{
    val (entity, requestUri) = requestSender.sendWithBody(new LoadCommand[T](parameters))
    try {
      new LoadResponse(entity, requestUri)
    } catch {
      case e: NullPointerException => throw new ResponseParseException("A Response JSON may be broken, or Version of Groonga is different:" + entity, e)
    }
  }
}
