package com.naokia.groonga4s.response

import com.fasterxml.jackson.databind.JsonNode
import com.naokia.groonga4s.util.column.JsonNodeConverter
import collection.JavaConversions._

case class DrillDown(key: Any, nsubrecs: Option[Int] = None, max: Option[Int] = None, min: Option[Int] = None, sum: Option[Int] = None, avg: Option[Int] = None)

/**
 * A parser for DrillDown nodes.
 */
package object DrillDownParser {
  val keyName = "_key"
  val nsubrecs = "_nsubrecs"
  val max = "_max"
  val min = "_min"
  val avg = "_avg"
  val sum = "_sum"

  /**
   * It converts Json node of drilldowns to Map
   * @param jsonNode root node of drill down.
   * @return
   */
  def parse(jsonNode: JsonNode): Map[String, Seq[DrillDown]] = {
    val nodes = jsonNode.fields().map {m => Map(m.getKey -> m.getValue)}.reduce((v1, v2) => v1 ++ v2)

    nodes.map { case (key, node) =>
      val hits = node.get(0).get(0)
      val outputColumns = node.get(1).map { column =>
        column.get(0).asText()
      }.toSeq
      val drillDowns = node.drop(2).map {
        _.zipWithIndex.map { case (value, i) =>
          val valueType = outputColumns(i)

          val castedValue = if (valueType == keyName) JsonNodeConverter.convert(value) else value.asInt()
          Map(valueType -> castedValue)
        }.reduce((v1, v2) => v1 ++ v2)
      }.map { map =>
        DrillDown(key = map(keyName), nsubrecs = map.get(nsubrecs).asInstanceOf[Option[Int]], max = map.get(max).asInstanceOf[Option[Int]], min = map.get(min).asInstanceOf[Option[Int]], avg = map.get(avg).asInstanceOf[Option[Int]], sum = map.get(sum).asInstanceOf[Option[Int]])
      }.toSeq
      Map(key -> drillDowns)
    }.reduce((v1, v2) => v1 ++ v2)
  }
}
