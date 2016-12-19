package com.naokia.groonga4s.request

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.naokia.groonga4s.util.column.SimpleLowerCaseWithUnderscoresStrategy
import com.naokia.groonga4s.util.mapping.CollectionConverter

case class LoadParameters[T](table: String, clazz: Class[T], values: Seq[T], ifExists: Option[Boolean]=None)

/**
 * Genarates query and convert case class to JSON for POST body
 *
 * @param loadParameters parameters for load command
 * @param convert2LowerCase If groonga table adopt snake case naming rule, this value must be true.
 * @tparam T type of case class for generate JSON body.
 */
class LoadCommand[T](loadParameters: LoadParameters[T], convert2LowerCase: Boolean = true) extends RequestWithBody{
  /**
   * Make and return query from LoadParameters.
   *
   * @return
   */
  override def toQuery: String = {
    val sb = new StringBuilder("/d/load?")
    sb.append("table=")
    sb.append(loadParameters.table)
    sb.append("&columns=")

    val keys = CollectionConverter.getPropertyNames(loadParameters.clazz).map { name =>
      if(convert2LowerCase) {
        SimpleLowerCaseWithUnderscoresStrategy.translate(name)
      } else {
        name
      }
    }
    sb.append(keys.mkString(","))
    if(loadParameters.ifExists.isDefined){
      sb.append("&ifexists=")
      val value = if(loadParameters.ifExists.get) "true" else "false"
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
    mapper.writeValueAsString(loadParameters.values)
  }
}
