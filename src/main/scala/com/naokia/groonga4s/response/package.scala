package com.naokia.groonga4s

package object response {
  trait Response{
  }

  trait ResponseParser[T, U <: Response] {
    def parse(jsonStr: String, query: String): U
  }
}
