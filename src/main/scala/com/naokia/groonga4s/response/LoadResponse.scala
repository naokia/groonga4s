package com.naokia.groonga4s.response

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.naokia.groonga4s.{Entity, RequestUri}

class LoadResponse(entity: Entity, val requestUri: RequestUri){
  private val mapper = new ObjectMapper
  mapper.registerModule(DefaultScalaModule)
  val rootNode = mapper.readValue(entity, classOf[JsonNode])
  val returnCode = rootNode.get(0).get(0).asInt
  val processStarted = rootNode.get(0).get(1).asDouble
  val processingTimes = rootNode.get(0).get(2).asDouble
  val affected = rootNode.get(1).asInt()
}
