package com.naokia.groonga4s.util.request

import org.specs2.mutable.Specification

class QuerySpec extends Specification{
  "Query" >> {
    "escape specific characters with back slash " >> {
      Query.escape(" ") must beEqualTo("""\ """)
      Query.escape(""""""") must beEqualTo("""\"""")
      Query.escape("''") must beEqualTo("""\'\'""")
      Query.escape("(abc)") must beEqualTo("""\(abc\)""")
      Query.escape("""\""") must beEqualTo("""\\""")
    }
  }
}
