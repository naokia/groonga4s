package com.naokia.groonga4s

package object command {
  trait Command{
    def getQuery: String
  }

  trait PostCommand extends Command{
    def getBody: String
  }

  trait Parameters{
  }
}
