package com.naokia.groonga4s.command

import org.specs2.mutable.Specification

class SelectCommandSpec extends Specification{
  case class Entry( _id: Int, _key: String, _score: Int)

   "SelectCommand generates path and query" >> {
     "with table name only" >>  {
       val command = new SelectCommand[Entry](SelectParameters("Entries", classOf[Entry]))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score")
     }

     "with query and matchColumns" >> {
       val query = QueryParameters("John", Seq("name"))
       val command = new SelectCommand[Entry](SelectParameters("Entries", classOf[Entry], query= Some(query)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&query=John&match_columns=name")
     }
     "with filter" >> {
       val command = new SelectCommand[Entry](SelectParameters("Entries", classOf[Entry], filter=Some("n_likes >= 5")))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&filter=n_likes+%3E%3D+5")
     }
     "with sortby" >> {
       val command = new SelectCommand[Entry](SelectParameters("Entries", classOf[Entry], sortby=Seq("_id")))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&sortby=_id")
     }
     "with scorer" >> {
       val command = new SelectCommand[Entry](SelectParameters("Entries", classOf[Entry], scorer=Some("_score = rand()")))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&scorer=%27_score+%3D+rand%28%29%27")
     }
     "with offset" >> {
       val command = new SelectCommand[Entry](SelectParameters("Entries", classOf[Entry], offset=Some(10)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&offset=10")
     }
     "with limit" >> {
       val command = new SelectCommand(SelectParameters("Entries", classOf[Entry], limit=Some(15)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&limit=15")
     }
     "with cache" >> {
       val command = new SelectCommand(SelectParameters("Entries", classOf[Entry], cache=Some(true)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&cache=yes")
     }
     "with no cache" >> {
       val command = new SelectCommand(SelectParameters("Entries", classOf[Entry], cache=Some(false)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&cache=no")
     }
     "with matchEscalationThreshold" >> {
       val query = QueryParameters("John", Seq("name"), matchEscalationThreshold=Some(-1))
       val command = new SelectCommand(SelectParameters("Entries", classOf[Entry], query= Some(query)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&query=John&match_columns=name&match_escalation_threshold=-1")
     }
     "with queryFlags" >> {
       val query = QueryParameters("John", Seq("name"), queryFlags = Some("ALLOW_PRAGMA|ALLOW_COLUMN|ALLOW_UPDATE|ALLOW_LEADING_NOT|NONE"))
       val command = new SelectCommand(SelectParameters("Entries", classOf[Entry], query= Some(query)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&query=John&match_columns=name&query_flags=ALLOW_PRAGMA%7CALLOW_COLUMN%7CALLOW_UPDATE%7CALLOW_LEADING_NOT%7CNONE")
     }
     "with queryExpander" >> {
       val query = QueryParameters("John", Seq("name"), queryExpander = Some("Thesaurus.synonym"))
       val command = new SelectCommand(SelectParameters("Entries", classOf[Entry], query= Some(query)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&query=John&match_columns=name&query_expander=Thesaurus.synonym")
     }
     "with adjuster" >> {
       val command = new SelectCommand(SelectParameters("Entries", classOf[Entry], adjuster = Some("""content @ "groonga" * 5""")))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&adjuster=content+%40+%22groonga%22+*+5")
     }
     "with drilldown" >> {
       val drilldown = DrillDownParameters("genre")
       val command = new SelectCommand(SelectParameters("Entries", classOf[Entry], drillDowns=Seq(drilldown)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre")
     }
     "with drilldownSortby" >> {
       val drilldown = DrillDownParameters("genre", sortby = Seq("-_nsubrecs", "_key"))
       val command = new SelectCommand(SelectParameters("Entries", classOf[Entry], drillDowns=Seq(drilldown)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.sortby=-_nsubrecs%2C_key")
     }
     "with drilldownOffset" >> {
       val drilldown = DrillDownParameters("genre", offset = Some(5))
       val command = new SelectCommand(SelectParameters("Entries", classOf[Entry], drillDowns=Seq(drilldown)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.offset=5")
     }
     "with drilldownOutputColumns" >> {
       val drilldown = DrillDownParameters("genre", outputColumns = Seq("_key", "_max"))
       val command = new SelectCommand(SelectParameters("Entries", classOf[Entry], drillDowns = Seq(drilldown)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.output_columns=_key%2C_max")
     }
     "with drilldownOutputColumns (no key, but key is to be add." >> {
       val drilldown = DrillDownParameters("genre", outputColumns = Seq("_max"))
       val command = new SelectCommand(SelectParameters("Entries", classOf[Entry], drillDowns = Seq(drilldown)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.output_columns=_max%2C_key")
     }
     "with drilldownLimit" >> {
       val drilldown = DrillDownParameters("genre", limit = Some(11))
       val command = new SelectCommand(SelectParameters("Entries", classOf[Entry], drillDowns = Seq(drilldown)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.limit=11")
     }
     "with drilldownCalcTypes" >> {
       val drilldown = DrillDownParameters("genre", calcTypes = Seq("MAX","MIN"))
       val command = new SelectCommand(SelectParameters("Entries", classOf[Entry], drillDowns = Seq(drilldown)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.calc_types=MAX%2CMIN")
     }
     "with drilldownCalcTarget" >> {
       val drilldown = DrillDownParameters("genre", calcTarget = Some("n_likes"))
       val command = new SelectCommand(SelectParameters("Entries", classOf[Entry], drillDowns = Seq(drilldown)))
       command.getQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.calc_target=n_likes")
     }
   }
 }
