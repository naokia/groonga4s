package com.naokia.groonga4s.util.column

import com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase

/**
 * This strategy don't omit leading underscore.
 */
object SimpleLowerCaseWithUnderscoresStrategy extends PropertyNamingStrategyBase{
  override def translate(propertyName: String): String = {
    propertyName.replaceAll("([A-Z+])([A-Z][a-z])", "$1_$2").replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase()
  }
}
