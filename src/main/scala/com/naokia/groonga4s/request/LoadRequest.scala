package com.naokia.groonga4s.request

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.naokia.groonga4s.util.column.SimpleLowerCaseWithUnderscoresStrategy
import com.naokia.groonga4s.util.mapping.CollectionConverter

/**
  * Generates query and convert case class to JSON for POST body
  *
  * @param table what to load
  * @param clazz
  * @param values
  * @param ifExists
  * @param convert2LowerCase If groonga table adopt snake case naming rule, this value must be true.
  * @tparam T type of case class for generate JSON body.
  */
class LoadRequest[T](table: String, clazz: Class[T], values: Seq[T], ifExists: Option[Boolean]=None, convert2LowerCase: Boolean = true) extends RequestWithBody{
  /**
   * Make and return query from LoadParameters.
   *
   * @return
   */
  override def toQuery: String = {
    val sb = new StringBuilder("/d/load?")
    sb.append("table=")
    sb.append(table)
    sb.append("&columns=")

    val keys = CollectionConverter.getPropertyNames(clazz).map { name =>
      if(convert2LowerCase) {
        SimpleLowerCaseWithUnderscoresStrategy.translate(name)
      } else {
        name
      }
    }
    sb.append(keys.mkString(","))
    if(ifExists.isDefined){
      sb.append("&ifexists=")
      val value = if(ifExists.get) "true" else "false"
      sb.append(value)
    }
    sb.toString()
  }

  /**
   * Converts list of case class to JSON body. and return it.
   *
   * @return JSON string.
   */
  override def getBody: String = {
    val mapper = new ObjectMapper().setPropertyNamingStrategy(SimpleLowerCaseWithUnderscoresStrategy)
    mapper.registerModule(DefaultScalaModule)
    mapper.writeValueAsString(values)
  }
}
