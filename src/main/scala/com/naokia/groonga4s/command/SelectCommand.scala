package com.naokia.groonga4s.command

import java.net.URLEncoder

case class SelectParameters(
  table: String,
  matchColumns: Seq[String]=Seq(),
  query: Option[String]=None,
  filter: Option[String]=None,
  scorer: Option[String]=None,
  sortby: Seq[String]=Seq(),
  outputColumns: Seq[String]=Seq(),
  offset: Option[Int]=None,
  limit: Option[Int]=None,
  drilldown: Seq[String]=Seq(),
  drilldownSortby: Seq[String]=Seq(),
  drilldownOutputColumns: Seq[String]=Seq(),
  drilldownOffset: Option[Int]=None,
  drilldownLimit: Option[Int]=None,
  cache: Option[Boolean]=None,
  matchEscalationThreshold: Option[String]=None,
  queryExpansion: Option[String]=None,
  queryFlags: Option[String]=None,
  queryExpander: Option[String]=None,
  adjuster: Option[String]=None,
  drilldownCalcTypes: Option[String]=None,
  drilldownCalcTarget: Option[String]=None
) extends Parameters{
  def stringify: String = {
    val sb = new StringBuilder()

    sb.append("table=" + table)
    sb.append(query.map("&query=" + URLEncoder.encode(_, "UTF-8")).getOrElse(""))

    sb.toString()
  }
}

/**
 * Created by naoki on 15/05/02.
 */
class SelectCommand(parameters: SelectParameters) extends Command{

  def stringify(): String = {
    "/d/select.json?" + parameters.stringify
  }
}
