package com.naokia.groonga4s

/**
 * Created by naoki on 15/05/03.
 */
package object command {
  trait Command extends URLParts{
  }

  trait Parameters{
  }

  trait URLParts {
    def stringify: String
  }
}
