package com.naokia.groonga4s.response

import java.util.Date

import org.specs2.mutable.Specification

/**
 * Created by naoki on 15/05/04.
 */

class SelectResponseParserSpec extends Specification{
  "SelectResponseParser" >> {
    "must parse Json with no item" >> {
      val jsonStr = """[[0,1430732073.06866,0.000226974487304688],[[[0],[["_id","UInt32"],["_key","ShortText"],["age","Int8"]]]]]"""

      val parser = new SelectResponseParser
      val response = parser.parse(jsonStr)
      response.returnCode must beEqualTo(0)
      response.processStarted must beEqualTo(1430732073.06866)
      response.processingTimes must beEqualTo(0.000226974487304688)
      response.hits must beEqualTo(0)
      response.items.size must beEqualTo(0)
    }
    "must parse Json with some items" >> {
      val jsonStr = """[[0,1430732073.06866,0.000226974487304688],[[[2],[["_id","UInt32"],["_key","ShortText"],["age","Int8"]],[1,"alice",18], [2,"john",20]]]]"""

      val parser = new SelectResponseParser
      val response = parser.parse(jsonStr)
      response.returnCode must beEqualTo(0)
      response.processStarted must beEqualTo(1430732073.06866)
      response.processingTimes must beEqualTo(0.000226974487304688)
      response.hits must beEqualTo(2)
      response.items must beAnInstanceOf[Seq[Map[String, Any]]]
      response.items.length must beEqualTo(2)
      response.items(0).get("_id") must beEqualTo(Some(1))
      response.items(0).get("_key") must beEqualTo(Some("alice"))
      response.items(0).get("age") must beEqualTo(Some(18))

      //TODO: parseに失敗した時どこで失敗したかわかりづらい

      //val response.get.body
    }
  }
}
