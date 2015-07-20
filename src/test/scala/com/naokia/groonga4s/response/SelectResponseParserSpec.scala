package com.naokia.groonga4s.response

import java.util.Date

import org.specs2.mutable.Specification

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
      response.items.head.get("_id") must beEqualTo(Some(1))
      response.items.head.get("_key") must beEqualTo(Some("alice"))
      response.items.head.get("age") must beEqualTo(Some(18))
    }

    "must parse Json with some items when hits is different from length of array.(ex: limit parameter used)" >> {
      val jsonStr = """[[0,1430732073.06866,0.000226974487304688],[[[10],[["_id","UInt32"],["_key","ShortText"],["age","Int8"]],[1,"alice",18], [2,"john",20]]]]"""

      val parser = new SelectResponseParser
      val response = parser.parse(jsonStr, "http://localhost:10041/d/select?table=Site&someQuery")
      response.hits must beEqualTo(10)
      response.items.length must beEqualTo(2)
    }

    "must parse Json with some items, and drilldowns" >> {
      val jsonStr = """[[0,1430822415.36609,5.05447387695312e-05],[[[4],[["_id","UInt32"],["_key","ShortText"],["age","Int8"],["city","ShortText"]],[1,"alice",18,"tokyo"],[2,"john",20,"osaka"],[5,"taro",30,"tokyo"],[6,"jiro",20,"osaka"]],[[3],[["_key","Int8"],["_nsubrecs","Int32"]],[18,1],[20,2],[30,1]]]]"""

      val parser = new SelectResponseParser
      val response = parser.parse(jsonStr, "http://localhost:10041/d/select?table=Site&someQuery")
      val a = response.drillDownGroups.get("default")
      response.drillDownGroups.get("default").get.length must beEqualTo(3)
      response.drillDownGroups.get("default").get.head.key must beEqualTo("18")
      response.drillDownGroups.get("default").get.head.nsubrecs must beEqualTo(Some(1))
    }

    "must parse Json with some items, and multi label drilldowns" >> {
      val jsonStr = """[[0,1436280531.61765,9.36985015869141e-05],[[[9],[["_id","UInt32"],["_key","ShortText"],["genre","ShortText"],["title","ShortText"],["user","Int64"]],[1,"http://example.org/","news","This is test record one!",1000],[2,"http://example.net/","sns","test record two.",9000],[3,"http://example.com/","news","test test record three.",4000],[4,"http://example.net/afr","news","test record four.",3000],[5,"http://example.org/aba","news","test test test record five.",3000],[6,"http://example.com/rab","news","test test test test record six.",1000],[7,"http://example.net/atv","news","test test test record seven.",4000],[8,"http://example.org/gat","news","test test record eight.",2000],[9,"http://example.com/vdw","news","test test record nine.",7000]],{"genre":[[2],[["_key","ShortText"],["_nsubrecs","Int32"]],["news",8],["sns",1]],"user":[[6],[["_key","Int64"],["_nsubrecs","Int32"]],[1000,2],[9000,1],[4000,2],[3000,2],[2000,1],[7000,1]]}]]"""

      val parser = new SelectResponseParser
      val response = parser.parse(jsonStr, "http://localhost:10041/d/select?table=Site&someQuery")
      val a = response.drillDownGroups.get("genre")

      response.drillDownGroups.get("genre").get.length must beEqualTo(2)
      response.drillDownGroups.get("user").get.head.key must beEqualTo("1000")
      response.drillDownGroups.get("user").get.head.nsubrecs must beEqualTo(Some(2))
    }
  }
}
