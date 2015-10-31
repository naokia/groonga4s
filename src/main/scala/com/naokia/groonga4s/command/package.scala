package com.naokia.groonga4s

package object command {
  trait Command{
    def getQuery: String
  }

  trait CommandWithBody extends Command{
    def getBody: String
  }

  trait Parameters{
  }
}
