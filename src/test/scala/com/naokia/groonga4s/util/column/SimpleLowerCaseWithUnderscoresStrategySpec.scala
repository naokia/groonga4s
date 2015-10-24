package com.naokia.groonga4s.util.column

import org.specs2.mutable.Specification

/**
 * Created by naoki on 15/10/23.
 */
class SimpleLowerCaseWithUnderscoresStrategySpec extends Specification {
  "SimpleLowerCaseWithUnderscoresStrategy.translate" >> {
    "convert camel case to snake case" >> {
      SimpleLowerCaseWithUnderscoresStrategy.translate("camelCase") must beEqualTo("camel_case")
    }

    "convert camel case to lower case (continuous uppercase)" >> {
      SimpleLowerCaseWithUnderscoresStrategy.translate("camelCASE") must beEqualTo("camel_case")
    }

    "convert camel case to lower case (three words)" >> {
      SimpleLowerCaseWithUnderscoresStrategy.translate("camelCaseName") must beEqualTo("camel_case_name")
    }

    "convert camel case to lower case (single word)" >> {
      SimpleLowerCaseWithUnderscoresStrategy.translate("name") must beEqualTo("name")
    }

    "convert camel case to lower case (with underscore)" >> {
      SimpleLowerCaseWithUnderscoresStrategy.translate("_key") must beEqualTo("_key")
    }
  }
}
