package com.naokia.groonga4s.util.request

/**
 * utils for query(free word search)
 */
object Query {
  def escape(target: String): String = {
    target.replaceAll("""([ \(\)'\"\\])""", """\\$1""")
  }
}
