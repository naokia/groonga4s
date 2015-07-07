package com.naokia.groonga4s.response

import org.specs2.mutable.Specification

/**
 * Created by naoki on 15/06/21.
 */
class ErrorResponseParserSpec extends Specification{
  "ErrorResponseParser" >> {
    "parse error json" >> {
      val jsonStr = "[[-22,1433725216.53196,0.000396728515625,\"invalid table name: <>\",[[\"grn_select\",\"proc.c\",1153]]]]"
      val response = new ErrorResponseParser().parse(jsonStr, "http://localhost:10041/d/select?table=Site&someQuery")
      response.returnCode must equalTo(-22)
      response.message must equalTo("invalid table name: <>")
    }
  }
}
