package com.naokia.groonga4s

package object command {
  trait Command{
    def stringify: String
  }

  trait Parameters{
  }
}
