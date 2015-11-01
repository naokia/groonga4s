package com.naokia.groonga4s.util.column

import com.fasterxml.jackson.databind.JsonNode
import collection.JavaConversions._

/**
 * An Object for convert JsonNode to Scala primitive types.
 */
object JsonNodeConverter extends ColumnConverter[JsonNode]{
  def convert(node: JsonNode): Any = {
    val names = node.fieldNames()
    node match {
      case node if node.isTextual => node.asText()
      case node if node.isInt => node.asInt()
      case node if node.isArray => node.elements().map{convert(_)}.toSeq
      case node if node.isLong => node.asLong()
      case node if node.isDouble => node.asDouble()
    }
  }
}
