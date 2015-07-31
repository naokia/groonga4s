package com.naokia.groonga4s.command

import java.net.URLEncoder

import com.naokia.groonga4s.util.request.Query

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
                           cache: Option[Boolean]=None,
                           matchEscalationThreshold: Option[Int]=None,
                           queryFlags: Option[String]=None,
                           queryExpander: Option[String]=None,
                           adjuster: Option[String]=None,
                           drilldowns: Seq[DrilldownParameters] = Seq()
) extends Parameters

case class DrilldownParameters(
                                key: String,
                                sortby: Seq[String]=Seq(),
                                offset: Option[Int]=None,
                                limit: Option[Int]=None,
                                outputColumns : Seq[String] = Seq(),
                                calcTypes: Seq[String] = Seq(),
                                calcTarget: Option[String]=None
                                )

/**
 * A converter of SelectParameters.
 */
class SelectCommand(parameters: SelectParameters) extends Command{
  val sb = new StringBuilder("/d/select.json?table=")
  sb.append(parameters.table)

  /**
   * It converts SelectCommand parameters to URL query.
   *
   * @return URL query
   */
  def stringify: String = {
    appendStringSeq("match_columns", parameters.matchColumns)
    appendEncodedString("query", parameters.query, escape = true)
    appendEncodedString("filter", parameters.filter)
    appendStringSeq("sortby", parameters.sortby)
    appendScript("scorer", parameters.scorer)
    appendStringSeq("output_columns", parameters.outputColumns)
    appendInt("offset", parameters.offset)
    appendInt("limit", parameters.limit)
    appendBoolean("cache", parameters.cache)
    appendInt("match_escalation_threshold", parameters.matchEscalationThreshold)
    appendEncodedString("query_flags", parameters.queryFlags)
    appendEncodedString("query_expander", parameters.queryExpander)
    appendEncodedString("adjuster", parameters.adjuster)

    appendDrillDownParameters()

    sb.toString()
  }

  /**
   * It passes parameters of drill down to StringBuilder.
   *
   * If "_key" is not in outputColumns although outputColumns expressly assigned, It adds key to outputColumns parameter.
   */
  private def appendDrillDownParameters() = {
    parameters.drilldowns.foreach { drilldownParameters =>
      val key = drilldownParameters.key
      appendEncodedString("keys", Some(key), Some(key))
      appendStringSeq("sortby", drilldownParameters.sortby, Some(key))
      appendInt("offset", drilldownParameters.offset, Some(key))
      appendInt("limit", drilldownParameters.limit, Some(key))
      val outputColumns = if(drilldownParameters.outputColumns.nonEmpty && ! drilldownParameters.outputColumns.contains(SelectCommand.outputColumnKey)){
        drilldownParameters.outputColumns :+ SelectCommand.outputColumnKey
      } else{
        drilldownParameters.outputColumns
      }
      appendStringSeq("output_columns", outputColumns, Some(key))
      appendStringSeq("calc_types", drilldownParameters.calcTypes, Some(key))
      appendEncodedString("calc_target", drilldownParameters.calcTarget, Some(key))
    }
  }

  private def appendEncodedString(columnName: String, str: Option[String], drillDownLabel : Option[String] = None, escape:Boolean = false) = {
    if(str.isDefined){
      appendColumnName(columnName, drillDownLabel)
      val target = if(escape) Query.escape(str.get) else str.get
      sb.append(encode(target))
    }
  }

  private def appendScript(columnName: String, str: Option[String]) = {
    if(str.isDefined){
      appendColumnName(columnName)
      sb.append(encode("'"))
      sb.append(encode(str.get))
      sb.append(encode("'"))
    }
  }

  private def appendStringSeq(columnName: String, seq: Seq[String], drillDownLabel : Option[String] = None) = {
    if(seq.nonEmpty) {
      appendColumnName(columnName, drillDownLabel)
      sb.append(encode(seq.mkString(",")))
    }
  }

  private def appendInt(columnName: String, num: Option[Int], drillDownLabel : Option[String] = None) = {
    if(num.isDefined){
      appendColumnName(columnName, drillDownLabel)
      sb.append(num.get)
    }
  }

  private def appendBoolean(columnName: String, bool: Option[Boolean]) = {
    if(bool.isDefined) {
      appendColumnName(columnName)
      bool.get match {
        case true => sb.append("yes")
        case false => sb.append("no")
      }
    }
  }

  private def appendDrilldownLabel(labelNum: String) = {
    sb.append("drilldown%5B")
    sb.append(labelNum)
    sb.append("%5D.")
  }

  private def appendColumnName(columnName: String, drillDownLabel : Option[String] = None) = {
    sb.append("&")
    if(drillDownLabel.isDefined) appendDrilldownLabel(drillDownLabel.get)
    sb.append(columnName)
    sb.append("=")
  }

  private def encode(str: String): String = {
    URLEncoder.encode(str, "UTF-8")
  }
}

object SelectCommand{
  val outputColumnKey = "_key"
}