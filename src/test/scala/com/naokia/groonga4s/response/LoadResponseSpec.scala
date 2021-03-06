package com.naokia.groonga4s.response

import org.specs2.mutable.Specification
import com.fasterxml.jackson.databind._

class LoadResponseSpec extends Specification {
  "LoadResponse" should {
    "parse" in {
      val jsonStr = """[[0,1446206383.85375,0.0631723403930664],1]"""
      val response = new LoadResponse(jsonStr, "query")
      response.returnCode must beEqualTo(0)
      response.requestUri must beEqualTo("query")
      response.processStarted must beEqualTo(1446206383.85375)
      response.processingTimes must beEqualTo(0.0631723403930664)
      response.affected must beEqualTo(1)
    }
  }
}
