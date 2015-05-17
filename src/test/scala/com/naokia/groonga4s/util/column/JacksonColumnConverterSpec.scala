package com.naokia.groonga4s.util.column

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node._
import org.specs2.mutable.Specification
import java.math.BigDecimal

/**
 * Created by naoki on 15/05/16.
 */
class JacksonColumnConverterSpec extends Specification{
  "JacksonColumnConverter" >> {
    "convert TextNode to String value" >> {
      val node = new TextNode("some text")

      JacksonColumnConverter.convert(node) must beEqualTo("some text")
    }
    "convert IntNode to Int value" >> {
      val node = new IntNode(19)

      JacksonColumnConverter.convert(node) must beEqualTo(19)
    }

    "convert ArrayNode to List" >> {
      val factory = JsonNodeFactory.instance
      val node = factory.arrayNode()
      node.add(19)
      node.add("some text")
      node.add(3)
      JacksonColumnConverter.convert(node) must beEqualTo(Seq(19,"some text", 3))
    }

    "convert LongNode to Long value" >> {
      val node = new LongNode(2147483648L)

      JacksonColumnConverter.convert(node) must beEqualTo(2147483648L)
    }

    "convert DoubleNode to Double value" >> {
      val node = new DoubleNode(19.8)

      JacksonColumnConverter.convert(node) must beEqualTo(19.8)
    }
  }
}
