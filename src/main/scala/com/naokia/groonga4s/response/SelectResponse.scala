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
case class SelectResponse(returnCode: Int, processStarted: Double, processingTimes: Double, hits: Int, items: Seq[Map[String, Any]]) extends Response

class SelectResponseParser extends ResponseParser{
  val mapper = new ObjectMapper
  mapper.registerModule(DefaultScalaModule)

  def parse(jsonStr: String): SelectResponse = {
    val rootNode = mapper.readValue(jsonStr, classOf[JsonNode])

    val returnCode = rootNode.get(0).get(0).asInt
    val processStarted = rootNode.get(0).get(1).asDouble()
    val processingTimes = rootNode.get(0).get(2).asDouble()
    val hits = rootNode.get(1).get(0).get(0).get(0).asInt()
    val columnNameList = (for(c <- rootNode.get(1).get(0).get(1).elements()) yield c.get(0).asText()).toList

    val entityList = if(hits > 0) {
      val origRowList = Range(2, hits +2).map( i =>
        rootNode.get(1).get(0).get(i)
      )
      array2Map(origRowList, columnNameList)
    } else{
      Seq()
    }

    //TODO: Drilldownを出力している時と処理分ける
    SelectResponse(returnCode, processStarted, processingTimes, hits, entityList)
  }

  private def collectColumnNames(columnListWithType: List[List[String]]):List[String] = {
    columnListWithType map{ columnWithType =>
      columnWithType(0)
    }
  }

  private def array2Map(origRowList: Seq[Any], columnNameList: Seq[String]): Seq[Map[String, Any]] = {
    origRowList map { origRow =>
      origRow.asInstanceOf[ArrayNode].toList.zipWithIndex.map { case (column, i) =>
        Map(columnNameList(i) -> JacksonColumnConverter.convert(column))
      }.reduce((a, b) => a ++ b)
    }
  }
}
