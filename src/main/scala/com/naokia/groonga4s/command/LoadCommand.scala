package com.naokia.groonga4s.command

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.naokia.groonga4s.util.column.NameConverter

case class LoadParameters[T](clazz: Class[T], values: Seq[T], table: String, ifExists: Option[Boolean]=None)

class LoadCommand[T](loadParameters: LoadParameters[T], convert2SnakeCase: Boolean = true) extends Command{
  def stringify: String = {
    val sb = new StringBuilder("/d/load?")
    sb.append("table=")
    sb.append(loadParameters.table)
    sb.append("&columns=")

    val keys = loadParameters.clazz.getDeclaredFields.map(_.getName).filter(!_.startsWith("$")).map { name =>
      if(convert2SnakeCase) {
        NameConverter.toSnakeCase(name)
      }else {
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
}
