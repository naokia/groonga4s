package com.naokia.groonga4s

/**
 * Created by naoki on 15/05/04.
 */
package object response {
  trait Response {

  }

  trait ResponseParser[T <: Response] {
    def parse(jsonStr: String): T
  }
}
