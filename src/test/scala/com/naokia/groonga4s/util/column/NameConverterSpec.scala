package com.naokia.groonga4s.util.column

import org.specs2.mutable.Specification

/**
 * Created by naoki on 15/10/23.
 */
class NameConverterSpec extends Specification {
  "NameConverterSpec.toSnakeCase" >> {
    "conert camel case to snake case" >> {
      NameConverter.toSnakeCase("camelCase") must beEqualTo("camel_case")
    }

    "conert camel case to snake case (continuous uppercase)" >> {
      NameConverter.toSnakeCase("camelCASE") must beEqualTo("camel_case")
    }

    "conert camel case to snake case (three words)" >> {
      NameConverter.toSnakeCase("camelCaseName") must beEqualTo("camel_case_name")
    }

    "conert camel case to snake case (single word)" >> {
      NameConverter.toSnakeCase("name") must beEqualTo("name")
    }

    "conert camel case to snake case (with underscore)" >> {
      NameConverter.toSnakeCase("_key") must beEqualTo("_key")
    }
  }
}
