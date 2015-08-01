package com.naokia.groonga4s.response

import com.fasterxml.jackson.databind.node.ArrayNode
import com.naokia.groonga4s.util.column.JsonNodeConverter
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import collection.JavaConversions._

/**
 * Response when select command is sent.
 *
 * @param returnCode groonga response code
 * @param query requested url
 * @param processStarted unix time when command is started
 * @param processingTimes elasped time
 * @param hits count of total hit records
 * @param items selected items
 * @param drillDownGroups a result of drill down
 */
case class SelectResponse(
                           returnCode: Int,
                           query: String,
                           processStarted: Double,
                           processingTimes: Double,
                           hits: Int,
                           items: Seq[Map[String, Any]],
                           drillDownGroups: Map[String, DrillDownLabeledGroup]=Map()) extends Response

/**
 * parser for response when select command is sent.
 */
class SelectResponseParser extends ResponseParser[SelectResponse]{
  val mapper = new ObjectMapper
  mapper.registerModule(DefaultScalaModule)

  /**
   * It converts groonga's response to SelectResponse
   *
   * @param jsonStr response body from groonga
   * @param query sent url
   * @return
   */
  def parse(jsonStr: String, query: String): SelectResponse = {
    val rootNode = mapper.readValue(jsonStr, classOf[JsonNode])

    val returnCode = rootNode.get(0).get(0).asInt
    val processStarted = rootNode.get(0).get(1).asDouble()
    val processingTimes = rootNode.get(0).get(2).asDouble()
    val hits = rootNode.get(1).get(0).get(0).get(0).asInt()
    val columnNames = (for(c <- rootNode.get(1).get(0).get(1).elements()) yield c.get(0).asText()).toList

    val entityList = if(hits > 0) {
      val size = rootNode.get(1).get(0).size
      val origRows = Range(2, size).map( i =>
        rootNode.get(1).get(0).get(i)
      )
      array2Map(origRows, columnNames)
    } else{
      Seq()
    }

    val drillDowns = if(rootNode.get(1).elements().size > 1) DrillDownParser.parse(rootNode.get(1).get(1)) else Map[String, DrillDownLabeledGroup]()

    SelectResponse(returnCode, query, processStarted, processingTimes, hits, entityList, drillDowns)
  }

  /**
   * It converts returned records to Map
   *
   * @param origRows records returned from groonga
   * @param columnNameList list of column name returned from groonga
   * @return
   */
  private def array2Map(origRows: Seq[JsonNode], columnNameList: Seq[String]): Seq[Map[String, Any]] = {
    origRows map { origRow =>
      origRow.asInstanceOf[ArrayNode].toList.zipWithIndex.map { case (column, i) =>
        Map(columnNameList(i) -> JsonNodeConverter.convert(column))
      }.reduce((c1, c2) => c1 ++ c2)
    }
  }
}
