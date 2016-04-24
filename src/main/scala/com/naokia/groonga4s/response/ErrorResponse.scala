package com.naokia.groonga4s.response

import com.fasterxml.jackson.databind.{ObjectMapper, JsonNode}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

case class ErrorResponse(returnCode: Int, query: String, message: String) extends Response

/**
 * Parser when the status code is not 200
 */
class ErrorResponseParser extends ResponseParser[Nothing, ErrorResponse]{
  override def parse(jsonStr: String, query: String): ErrorResponse = {
    val mapper = new ObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val rootNode = mapper.readValue(jsonStr, classOf[JsonNode])
    val returnCode = rootNode.get(0).get(0).asInt
    val message = rootNode.get(0).get(3).asText()

    ErrorResponse(returnCode, query, message)
  }
}
