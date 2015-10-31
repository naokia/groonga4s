package com.naokia.groonga4s.command

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.naokia.groonga4s.util.column.SimpleLowerCaseWithUnderscoresStrategy

case class LoadParameters[T](clazz: Class[T], values: Seq[T], table: String, ifExists: Option[Boolean]=None)

class LoadCommand[T](loadParameters: LoadParameters[T], convert2LowerCase: Boolean = true) extends CommandWithBody{
  override def getQuery: String = {
    val sb = new StringBuilder("/d/load?")
    sb.append("table=")
    sb.append(loadParameters.table)
    sb.append("&columns=")

    val keys = loadParameters.clazz.getDeclaredFields.map(_.getName).filter(!_.startsWith("$")).map { name =>
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

  override def getBody: String = {
    val mapper = new ObjectMapper().setPropertyNamingStrategy(SimpleLowerCaseWithUnderscoresStrategy)
    mapper.registerModule(DefaultScalaModule)
    mapper.writeValueAsString(loadParameters.values)
  }
}
