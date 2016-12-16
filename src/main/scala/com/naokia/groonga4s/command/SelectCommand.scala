package com.naokia.groonga4s.command

import java.net.URLEncoder
import com.naokia.groonga4s.util.request.Query

class SelectParameters private(builder: SelectParameters.Builder) extends Command{
  val table: String = builder.table
  val outputColumns: Seq[String] = builder.outputColumns
  val query: Option[String] = builder.query
  val filter: Option[String] = builder.filter
  val scorer: Option[String] = builder.scorer
  val sortBy: Seq[String] = builder.sortBy
  val offset: Option[Int] = builder.offset
  val limit: Option[Int] = builder.limit
  val cache: Option[Boolean] = builder.cache
  val adjuster: Option[String] = builder.adjuster
  val drillDowns: Seq[DrillDownParameters] = builder.drillDowns
  val matchColumns: Seq[String] = builder.matchColumns
  val queryFlags: Option[String] = builder.queryFlags
  val queryExpander: Option[String] = builder.queryExpander
  val matchEscalationThreshold: Option[Int] = builder.matchEscalationThreshold

  private val sb = new StringBuilder("/d/select.json?table=")
  sb.append(table)

  /**
    * Converts SelectCommand parameters to URL query.
    *
    * @return URL query
    */
  def toQuery: String = {
    appendStringSeq("output_columns", outputColumns)
    appendEncodedString("filter", filter)
    appendStringSeq("sortby", sortBy)
    appendScript("scorer", scorer)
    appendInt("offset", offset)
    appendInt("limit", limit)
    appendBoolean("cache", cache)
    appendEncodedString("adjuster", adjuster)

    appendEncodedString("query", query)
    appendStringSeq("match_columns", matchColumns)
    appendInt("match_escalation_threshold", matchEscalationThreshold)
    appendEncodedString("query_flags", queryFlags)
    appendEncodedString("query_expander", queryExpander)


    appendDrillDownParameters()

    sb.toString()
  }

  /**
    * Passes parameters of drill down to StringBuilder.
    *
    * If "_key" is not in outputColumns although outputColumns expressly assigned, It adds key to outputColumns parameter.
    */
  private def appendDrillDownParameters() = {
    drillDowns.foreach { drillDownParameters =>
      val key = drillDownParameters.key
      appendEncodedString("keys", Some(key), Some(key))
      appendStringSeq("sortby", drillDownParameters.sortby, Some(key))
      appendInt("offset", drillDownParameters.offset, Some(key))
      appendInt("limit", drillDownParameters.limit, Some(key))
      val outputColumns = if(drillDownParameters.outputColumns.nonEmpty && ! drillDownParameters.outputColumns.contains(SelectParameters.outputColumnKey)){
        drillDownParameters.outputColumns :+ SelectParameters.outputColumnKey
      } else{
        drillDownParameters.outputColumns
      }
      appendStringSeq("output_columns", outputColumns, Some(key))
      appendStringSeq("calc_types", drillDownParameters.calcTypes, Some(key))
      appendEncodedString("calc_target", drillDownParameters.calcTarget, Some(key))
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

  private def appendDrillDownLabel(labelNum: String) = {
    sb.append("drilldown%5B")
    sb.append(labelNum)
    sb.append("%5D.")
  }

  private def appendColumnName(columnName: String, drillDownLabel : Option[String] = None) = {
    sb.append("&")
    if(drillDownLabel.isDefined) appendDrillDownLabel(drillDownLabel.get)
    sb.append(columnName)
    sb.append("=")
  }

  private def encode(str: String): String = {
    URLEncoder.encode(str, "UTF-8")
  }
}

object SelectParameters{
  val outputColumnKey = "_key"

  class Builder(val table: String){
    private[SelectParameters] var outputColumns: Seq[String] = Seq()
    private[SelectParameters] var query: Option[String] = None
    private[SelectParameters] var filter: Option[String]=None
    private[SelectParameters] var scorer: Option[String]=None
    private[SelectParameters] var sortBy: Seq[String]=Seq()
    private[SelectParameters] var offset: Option[Int]=None
    private[SelectParameters] var limit: Option[Int]=None
    private[SelectParameters] var cache: Option[Boolean]=None
    private[SelectParameters] var adjuster: Option[String]=None
    private[SelectParameters] var drillDowns: Seq[DrillDownParameters] = Seq()
    private[SelectParameters] var matchColumns: Seq[String] = Seq()
    private[SelectParameters] var queryFlags: Option[String]=None
    private[SelectParameters] var queryExpander: Option[String]=None
    private[SelectParameters] var matchEscalationThreshold: Option[Int]=None


    def withOutputColumns(value: Seq[String]) = {
      outputColumns = value
      this
    }
    def withQuery(value: String) = {
      query = Some(value)
      this
    }
    def withFilter(value: String) = {
      filter = Some(value)
      this
    }
    def withScorer(value: String) = {
      scorer = Some(value)
      this
    }
    def withSortBy(value: Seq[String]) = {
      sortBy = value
      this
    }
    def withOffset(value: Int) = {
      offset = Some(value)
      this
    }
    def withLimit(value: Int) = {
      limit = Some(value)
      this
    }
    def withCache(value: Boolean) = {
      cache = Some(value)
      this
    }
    def withAdjuster(value: String) = {
      adjuster = Some(value)
      this
    }
    def withDrillDowns(value: Seq[DrillDownParameters]) = {
      drillDowns = value
      this
    }
    def withMatchColumns(value: Seq[String]) = {
      matchColumns = value
      this
    }
    def withQueryFlags(value: String) = {
      queryFlags = Some(value)
      this
    }
    def withQueryExpander(value: String) = {
      queryExpander = Some(value)
      this
    }
    def withMatchEscalationThreshold(value: Int) = {
      matchEscalationThreshold = Some(value)
      this
    }

    def build: SelectParameters = {
      new SelectParameters(this)
    }
  }
}

case class DrillDownParameters(
                                key: String,
                                sortby: Seq[String]=Seq(),
                                offset: Option[Int]=None,
                                limit: Option[Int]=None,
                                outputColumns : Seq[String] = Seq(),
                                calcTypes: Seq[String] = Seq(),
                                calcTarget: Option[String]=None
                                ) extends Parameters

