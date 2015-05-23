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
  drilldownOffset: Option[Int]=None,
  drilldownLimit: Option[Int]=None,
  cache: Option[Boolean]=None,
  matchEscalationThreshold: Option[Int]=None,
  queryFlags: Option[String]=None,
  queryExpander: Option[String]=None,
  adjuster: Option[String]=None,
  drilldownCalcTypes: Option[String]=None,
  drilldownCalcTarget: Option[String]=None
) extends Parameters{
}

/**
 * Created by naoki on 15/05/02.
 */
class SelectCommand(parameters: SelectParameters) extends Command{
  val sb = new StringBuilder("/d/select.json?table=")
  sb.append(parameters.table)

  def stringify(): String = {
    appendEncodedString("query", parameters.query)
    appendEncodedString("filter", parameters.filter)
    appendEncodedString("scorer", parameters.scorer)
    appendStringSeq("output_columns", parameters.outputColumns)
    appendInt("offset", parameters.offset)
    appendInt("limit", parameters.limit)
    appendStringSeq("drilldown", parameters.drilldown)
    appendStringSeq("drilldown_sortby", parameters.drilldownSortby)
    appendInt("drilldown_offset", parameters.drilldownOffset)
    appendInt("drilldown_limit", parameters.drilldownLimit)
    appendBoolean("cache", parameters.cache)
    appendInt("match_escalation_threshold", parameters.matchEscalationThreshold)
    appendEncodedString("query_flags", parameters.queryFlags)
    appendEncodedString("query_expander", parameters.queryExpander)
    appendEncodedString("adjuster", parameters.adjuster)
    appendEncodedString("drilldown_calc_types", parameters.drilldownCalcTypes)
    appendEncodedString("drilldown_calc_target", parameters.drilldownCalcTarget)

    sb.toString()
  }

  private def appendEncodedString(columnName: String, str: Option[String]) = {
    if(str.isDefined == true){
      appendColumnName(columnName)
      sb.append(encode(str.get))
    }
  }

  private def appendStringSeq(columnName: String, seq: Seq[String]) = {
    if(seq.length > 0) {
      appendColumnName(columnName)
      sb.append(encode(seq.mkString(",")))
    }
  }

  private def appendInt(columnName: String, num: Option[Int]) = {
    if(num.isDefined == true){
      appendColumnName(columnName)
      sb.append(num.get)
    }
  }

  private def appendBoolean(columnName: String, bool: Option[Boolean]) = {
    if(bool.isDefined == true) {
      appendColumnName(columnName)
      bool.get match {
        case true => sb.append("yes")
        case false => sb.append("no")
      }
    }
  }

  private def appendColumnName(columnName: String) = {
    sb.append("&")
    sb.append(columnName)
    sb.append("=")
  }

  private def encode(str: String): String = {
    URLEncoder.encode(str, "UTF-8")
  }
}
