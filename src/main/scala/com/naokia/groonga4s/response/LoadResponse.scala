package com.naokia.groonga4s.response

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

case class LoadResponse(
                         returnCode: Int,
                         query: String,
                         processStarted: Double,
                         processingTimes: Double,
                         affected: Int
                         ) extends Response

/**
 * LoadResponseParser parse JSON for load command.
 */
class LoadResponseParser extends ResponseParser[Nothing, LoadResponse]{
  override def parse(jsonStr: String, query: String): LoadResponse = {
    val mapper = new ObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val rootNode = mapper.readValue(jsonStr, classOf[JsonNode])
    val returnCode = rootNode.get(0).get(0).asInt
    val processStarted = rootNode.get(0).get(1).asDouble
    val processingTimes = rootNode.get(0).get(2).asDouble
    val affected = rootNode.get(1).asInt()

    LoadResponse(returnCode, query, processStarted, processingTimes, affected)
  }
}
