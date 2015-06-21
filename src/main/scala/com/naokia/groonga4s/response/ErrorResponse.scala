package com.naokia.groonga4s.response

import com.fasterxml.jackson.databind.{ObjectMapper, JsonNode}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

case class ErrorResponse(code: Int, message: String) extends Response

/**
 * Created by naoki on 15/06/21.
 */
class ErrorResponseParser extends ResponseParser[ErrorResponse]{
  override def parse(jsonStr: String): ErrorResponse = {
    val mapper = new ObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val rootNode = mapper.readValue(jsonStr, classOf[JsonNode])
    val returnCode = rootNode.get(0).get(0).asInt
    val message = rootNode.get(0).get(3).asText()

    ErrorResponse(returnCode, message)
  }
}
