package com.naokia.groonga4s.response

import com.fasterxml.jackson.databind.node.ArrayNode
import com.naokia.groonga4s.util.column.JsonNodeConverter
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.naokia.groonga4s.{Entity, RequestUri, ResponseParseException}
import com.naokia.groonga4s.util.mapping.CollectionConverter

import collection.JavaConversions._
import scala.reflect.runtime.universe._
import scala.reflect.{ClassTag, classTag}

/**
 * Response when select command is sent.
 *
 * @param entity JSON
 * @param requestUri requested url
 */
class SelectResponse(entity: Entity, val requestUri: RequestUri) extends Response {
  private val mapper = new ObjectMapper
  mapper.registerModule(DefaultScalaModule)
  private val rootNode = mapper.readValue(entity, classOf[JsonNode])
  private val columnNames = (for (c <- rootNode.get(1).get(0).get(1).elements()) yield c.get(0).asText()).toList
  private val documentNode = rootNode.get(1).get(0)

  val returnCode = rootNode.get(0).get(0).asInt
  val processStarted = rootNode.get(0).get(1).asDouble()
  val processingTimes = rootNode.get(0).get(2).asDouble()
  val hits = rootNode.get(1).get(0).get(0).get(0).asInt()
  val drillDowns = if (rootNode.get(1).elements().size > 1) DrillDownParser.parse(rootNode.get(1).get(1)) else Map[String, DrillDownLabeledGroup]()

  def as[T: TypeTag: ClassTag]: Seq[T] = {
    val tpe = typeOf[T]
    parse(item => CollectionConverter.map2class[T](item))
  }

  def asMap: Seq[Map[String, Any]] = {
    parse(item => item)
  }

  private def parse[U](f: Map[String, Any] => U): Seq[U] = {
   if (hits > 0) {
      val size = documentNode.size
      val origRows = Range(2, size).map(i =>
        documentNode.get(i)
      )

      array2Map(origRows, columnNames).map(item =>
        f(item)
      )
    } else {
      Seq[U]()
    }
  }

  /**
    * Converts returned records to Map
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
