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
      val response = parser.parse(jsonStr, "http://localhost:10041/d/select?table=Site&someQuery")
      response.returnCode must beEqualTo(0)
      response.processStarted must beEqualTo(1430732073.06866)
      response.processingTimes must beEqualTo(0.000226974487304688)
      response.hits must beEqualTo(0)
      response.items.size must beEqualTo(0)
      response.query must beEqualTo("http://localhost:10041/d/select?table=Site&someQuery")
    }
    "must parse Json with some items" >> {
      val jsonStr = """[[0,1430732073.06866,0.000226974487304688],[[[2],[["_id","UInt32"],["_key","ShortText"],["age","Int8"]],[1,"alice",18], [2,"john",20]]]]"""

      val parser = new SelectResponseParser
      val response = parser.parse(jsonStr, "http://localhost:10041/d/select?table=Site&someQuery")
      response.returnCode must beEqualTo(0)
      response.processStarted must beEqualTo(1430732073.06866)
      response.processingTimes must beEqualTo(0.000226974487304688)
      response.hits must beEqualTo(2)
      response.items must beAnInstanceOf[Seq[Map[String, Any]]]
      response.items.length must beEqualTo(2)
      response.items(0).get("_id") must beEqualTo(Some(1))
      response.items(0).get("_key") must beEqualTo(Some("alice"))
      response.items(0).get("age") must beEqualTo(Some(18))
    }

    "must parse Json with some items when hits is different from length of array.(ex: limit parameter used)" >> {
      val jsonStr = """[[0,1430732073.06866,0.000226974487304688],[[[10],[["_id","UInt32"],["_key","ShortText"],["age","Int8"]],[1,"alice",18], [2,"john",20]]]]"""

      val parser = new SelectResponseParser
      val response = parser.parse(jsonStr, "http://localhost:10041/d/select?table=Site&someQuery")
      response.hits must beEqualTo(10)
      response.items.length must beEqualTo(2)
    }

    "must parse Json with some items, and drilldowns" >> {
      val jsonStr = """[[0,1430822415.36609,5.05447387695312e-05],[[[4],[["_id","UInt32"],["_key","ShortText"],["age","Int8"],["city","ShortText"]],[1,"alice",18,"tokyo"],[2,"john",20,"osaka"],[5,"taro",30,"tokyo"],[6,"jiro",20,"osaka"]],[[3],[["_key","Int8"],["_nsubrecs","Int32"]],[18,1],[20,2],[30,1]],[[2],[["_key","ShortText"],["_nsubrecs","Int32"]],["tokyo",2],["osaka",2]]]]"""

      val parser = new SelectResponseParser
      val response = parser.parse(jsonStr, "http://localhost:10041/d/select?table=Site&someQuery")
      response.drilldowns.length must beEqualTo(2)
      response.drilldowns(0).get(20) must beEqualTo(Some(2))
      response.drilldowns(1).get("tokyo") must beEqualTo(Some(2))
    }
  }
}
