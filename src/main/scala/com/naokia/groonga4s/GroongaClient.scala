package com.naokia.groonga4s

import com.naokia.groonga4s.command._
import com.naokia.groonga4s.protocol.HttpRequestSender
import com.naokia.groonga4s.response._

import scala.reflect.{ClassTag, classTag}
import scala.util.Try
import scala.reflect.runtime.universe._

trait Client{
  def select[T: TypeTag: ClassTag](parameters: SelectParameters[T]): Try[SelectResponse[T]]
  def load[T](parameters: LoadParameters[T]): Try[LoadResponse]
}

/**
 * A client for groonga.
 *
 * @param uri scheme and host name.
 */
class GroongaClient(uri: String) extends Client {
  val requestSender = new HttpRequestSender(uri)

  /**
   * It requests select command to groonga.
   *
   * @param parameters parameter set
   * @return
   */
  def select[T: TypeTag: ClassTag](parameters: SelectParameters[T]) = {
    val tt = typeTag[T]
    val ct = classTag[T]
    requestSender.send[T,SelectResponse[T]](new SelectCommand(parameters), new SelectResponseParser[T])
  }

  /**
   * It requests load command to groonga.
   *
   * @param parameters
   * @tparam T
   * @return
   */
  def load[T](parameters: LoadParameters[T]): Try[LoadResponse] = {
    requestSender.sendWithBody(new LoadCommand[T](parameters), new LoadResponseParser)
  }
}
