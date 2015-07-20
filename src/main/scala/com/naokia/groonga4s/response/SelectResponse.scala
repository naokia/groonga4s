package com.naokia.groonga4s.response

import com.fasterxml.jackson.databind.node.ArrayNode
import com.naokia.groonga4s.util.column.JacksonColumnConverter
import scala.util.Try
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import collection.JavaConversions._

/**
 * response when select command is sent.
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
                           drillDownGroups: Map[String, Seq[DrillDown]]=Map()) extends Response

case class DrillDown(key: String, nsubrecs: Option[Int] = None, max: Option[Int] = None, min: Option[Int] = None, sum: Option[Int] = None, avg: Option[Int] = None)

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
    val drilldowns = if(rootNode.get(1).elements().size > 1) parseDrilldowns(rootNode.get(1).get(1)) else Map[String, Seq[DrillDown]]()

    SelectResponse(returnCode, query, processStarted, processingTimes, hits, entityList, drilldowns)
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
        Map(columnNameList(i) -> JacksonColumnConverter.convert(column))
      }.reduce((c1, c2) => c1 ++ c2)
    }
  }

  /**
   * It converts Json node of drilldowns to Map
   * @param jsonNode root node of drill down.
   * @return
   */
  private def parseDrilldowns(jsonNode: JsonNode): Map[String, Seq[DrillDown]] = {
    val nodes = if(jsonNode.isObject){
      jsonNode.fields().map {m => Map(m.getKey -> m.getValue)}.reduce((v1, v2) => v1 ++ v2)
    } else{
      Map(new String("default") -> jsonNode)
    }
    nodes.map { case (key, node) =>
      val hits = node.get(0).get(0)
      val outputColumns = node.get(1).map { column =>
        column.get(0).asText()
      }.toSeq
      val drillDowns = node.drop(2).map {
        _.zipWithIndex.map { case (value, i) =>
          val valueType = outputColumns(i)

          val castedValue = if (valueType == "_key") value else value.asInt()
          Map(valueType -> castedValue)
        }.reduce((v1, v2) => v1 ++ v2)
      }.map { map =>
        DrillDown(key = map("_key").toString, nsubrecs = map.get("_nsubrecs").asInstanceOf[Option[Int]], max = map.get("_max").asInstanceOf[Option[Int]], min = map.get("_min").asInstanceOf[Option[Int]], avg = map.get("_avg").asInstanceOf[Option[Int]], sum = map.get("_sum").asInstanceOf[Option[Int]])
      }.toSeq
      Map(key -> drillDowns)
    }.reduce((v1, v2) => v1 ++ v2)
  }
}
