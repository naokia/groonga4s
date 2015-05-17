package com.naokia.groonga4s

import com.naokia.groonga4s.command.SelectParameters
import com.naokia.groonga4s.response.SelectResponse
import org.specs2.mutable.Specification

import play.api.libs.json
import play.api.libs.json.Json

/**
 * Created by naoki on 15/04/29.
 */

class GroongaClientSpec extends Specification{
  "GroongaClient Specification" >> {
    "GroongaClient must find rows with select command." >>  {
      val client = new GroongaClient("http://localhost:10041")
      val response = client.select(SelectParameters("Entries"))
      response must beSuccessfulTry
      //response.get must anInstanceOf[SelectResponse]
    }

    "GroongaClient must throw an exception when some errors occurred." >> {
      val client = new GroongaClient("http://invalid_url:10041")
      val response = client.select(SelectParameters("Entries"))
      response must beFailedTry
    }
  }
}
