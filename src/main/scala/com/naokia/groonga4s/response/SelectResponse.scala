package com.naokia.groonga4s.response

import com.fasterxml.jackson.databind.node.ArrayNode
import com.naokia.groonga4s.util.column.JacksonColumnConverter
import scala.util.Try
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import collection.JavaConversions._

/**
 * Created by naoki on 15/04/29.
 */
case class SelectResponse(returnCode: Int, query: String, processStarted: Double, processingTimes: Double, hits: Int, items: Seq[Map[String, Any]], drilldowns: Seq[Map[Any,Any]]=Seq()) extends Response

class SelectResponseParser extends ResponseParser[SelectResponse]{
  val mapper = new ObjectMapper
  mapper.registerModule(DefaultScalaModule)

  def parse(jsonStr: String, query: String): SelectResponse = {
    val rootNode = mapper.readValue(jsonStr, classOf[JsonNode])

    val returnCode = rootNode.get(0).get(0).asInt
    val processStarted = rootNode.get(0).get(1).asDouble()
    val processingTimes = rootNode.get(0).get(2).asDouble()
    val hits = rootNode.get(1).get(0).get(0).get(0).asInt()
    val columnNameList = (for(c <- rootNode.get(1).get(0).get(1).elements()) yield c.get(0).asText()).toList

    val entityList = if(hits > 0) {
      val size = rootNode.get(1).get(0).size
      val origRowList = Range(2, size).map( i =>
        rootNode.get(1).get(0).get(i)
      )
      array2Map(origRowList, columnNameList)
    } else{
      Seq()
    }
    val drilldowns = if(rootNode.get(1).elements().size > 1) parseDrilldowns(rootNode.get(1).elements()) else Seq()

    SelectResponse(returnCode, query, processStarted, processingTimes, hits, entityList, drilldowns)
  }

  private def array2Map(origRowList: Seq[JsonNode], columnNameList: Seq[String]): Seq[Map[String, Any]] = {
    origRowList map { origRow =>
      origRow.asInstanceOf[ArrayNode].toList.zipWithIndex.map { case (column, i) =>
        Map(columnNameList(i) -> JacksonColumnConverter.convert(column))
      }.reduce((c1, c2) => c1 ++ c2)
    }
  }

  private def parseDrilldowns(nodeList: Iterator[JsonNode]): Seq[Map[Any,Any]] = {
    nodeList.drop(1).map { drillDownList =>
      drillDownList.drop(2).map { valueList =>
        val key = JacksonColumnConverter.convert(valueList.get(0))
        val value = JacksonColumnConverter.convert(valueList.get(1))
        Map(key -> value)
      }.reduce((v1, v2) => v1 ++ v2)
    }.toSeq
  }
}