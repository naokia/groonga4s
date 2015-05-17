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

      case class Entry(_id: Int, _key: String, age: Int)
      val parser = new SelectResponseParser[Entry]
      val response = parser.parse(jsonStr)
      response must beSuccessfulTry
      //response.get must beAnInstanceOf[SelectResponse]
      response.get.returnCode must beEqualTo(0)
      response.get.processStarted must beEqualTo(1430732073.06866)
      response.get.processingTimes must beEqualTo(0.000226974487304688)
      response.get.hits must beEqualTo(0)
      response.get.items.size must beEqualTo(0)
    }
    "must parse Json with some items" >> {
      val jsonStr = """[[0,1430732073.06866,0.000226974487304688],[[[2],[["_id","UInt32"],["_key","ShortText"],["age","Int8"]],[1,"alice",18], [2,"john",20]]]]"""

      case class Entry(_id: Int, _key: String, age: Int)
      val parser = new SelectResponseParser[Entry]
      val response = parser.parse(jsonStr)
      response must beSuccessfulTry
      //response.get must beAnInstanceOf[SelectResponse]
      response.get.returnCode must beEqualTo(0)
      response.get.processStarted must beEqualTo(1430732073.06866)
      response.get.processingTimes must beEqualTo(0.000226974487304688)
      response.get.hits must beEqualTo(2)
      response.get.items must beAnInstanceOf[Seq[Entry]]
      response.get.items.length must beEqualTo(2)
      response.get.items(0).get("_id") must beEqualTo(Some(1))
      response.get.items(0).get("_key") must beEqualTo(Some("alice"))
      response.get.items(0).get("age") must beEqualTo(Some(18))

      //TODO: parseに失敗した時どこで失敗したかわかりづらい

      //val response.get.body
    }
  }
}
