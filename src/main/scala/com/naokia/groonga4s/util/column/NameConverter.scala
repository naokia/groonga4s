package com.naokia.groonga4s.util.column

/**
 * Created by naoki on 15/10/23.
 */
object NameConverter {
  def toSnakeCase(string: String): String = {
    string.replaceAll("([A-Z+])([A-Z][a-z])", "$1_$2").replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase()
  }
}
