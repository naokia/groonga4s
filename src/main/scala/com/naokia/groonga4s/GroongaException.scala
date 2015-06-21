package com.naokia.groonga4s

/**
 * Created by naoki on 15/06/21.
 */
case class GroongaException(code: Int, httpCode: Int, message: String, query: String) extends Exception{
  override def getMessage: String = {
    message
  }
}
