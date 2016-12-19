package com.naokia.groonga4s

package object request {
  trait Request{
    def toQuery: String
  }

  trait RequestWithBody extends Request{
    def getBody: String
  }
}
