package com.naokia.groonga4s.util.column

import com.fasterxml.jackson.databind.JsonNode
import collection.JavaConversions._

/**
 * Created by naoki on 15/05/16.
 */
object JacksonColumnConverter extends ColumnConverter[JsonNode]{
  def convert(node: JsonNode): Any = {
    node match {
      case node if node.isTextual => node.asText()
      case node if node.isInt => node.asInt()
      case node if node.isArray => node.elements().map{convert(_)}.toSeq
      case node if node.isLong => node.asLong()
      case node if node.isDouble => node.asDouble()
    }
  }
}
