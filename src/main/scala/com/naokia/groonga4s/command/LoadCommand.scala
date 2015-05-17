package com.naokia.groonga4s.command

/**
 * Created by naoki on 15/05/03.
 */
class LoadCommand(values: String, table: String, columns: Seq[String], ifexists: Option[String]=None) extends Command{
  def stringify: String = {
    val sb = new StringBuilder("/d/load?")
    sb.append("values=")
    sb.append(values)
    sb.append("&table=")
    sb.append(table)
    sb.append("&columns=")
    sb.append(columns.mkString(","))
    if(ifexists.isDefined){
      sb.append("&ifexists=")
      sb.append(ifexists.get)
    }
    sb.toString()
  }
}
