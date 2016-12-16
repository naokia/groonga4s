package com.naokia.groonga4s.command

import org.specs2.mutable.Specification

class SelectCommandSpec extends Specification{
  case class Entry( _id: Int, _key: String, _score: Int)

   "SelectCommand generates path and query" >> {
     "with table name only" >>  {
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score")
     }

     "with query and matchColumns" >> {
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withQuery("John")
         .withMatchColumns(Seq("name"))
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&query=John&match_columns=name")
     }
     "with filter" >> {
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withFilter("n_likes >= 5").build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&filter=n_likes+%3E%3D+5")
     }
     "with sortBy" >> {
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withSortBy(Seq("_id"))
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&sortby=_id")
     }
     "with scorer" >> {
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withScorer("_score = rand()")
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&scorer=%27_score+%3D+rand%28%29%27")
     }
     "with offset" >> {
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withOffset(10)
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&offset=10")
     }
     "with limit" >> {
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withLimit(15)
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&limit=15")
     }
     "with cache" >> {
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withCache(true)
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&cache=yes")
     }
     "with no cache" >> {
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withCache(false)
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&cache=no")
     }
     "with matchEscalationThreshold" >> {
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withQuery("John")
         .withMatchColumns(Seq("name"))
         .withMatchEscalationThreshold(-1)
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&query=John&match_columns=name&match_escalation_threshold=-1")
     }
     "with queryFlags" >> {
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withQuery("John")
         .withMatchColumns(Seq("name"))
         .withQueryFlags("ALLOW_PRAGMA|ALLOW_COLUMN|ALLOW_UPDATE|ALLOW_LEADING_NOT|NONE")
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&query=John&match_columns=name&query_flags=ALLOW_PRAGMA%7CALLOW_COLUMN%7CALLOW_UPDATE%7CALLOW_LEADING_NOT%7CNONE")
     }
     "with queryExpander" >> {
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withQuery("John")
         .withMatchColumns(Seq("name"))
         .withQueryExpander("Thesaurus.synonym")
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&query=John&match_columns=name&query_expander=Thesaurus.synonym")
     }
     "with adjuster" >> {
       val command = new SelectParameters.Builder("Entries")
         .withAdjuster("""content @ "groonga" * 5""")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&adjuster=content+%40+%22groonga%22+*+5")
     }
     "with drilldown" >> {
       val drilldown = DrillDownParameters("genre")
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre")
     }
     "with drilldownSortby" >> {
       val drilldown = DrillDownParameters("genre", sortby = Seq("-_nsubrecs", "_key"))
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.sortby=-_nsubrecs%2C_key")
     }
     "with drilldownOffset" >> {
       val drilldown = DrillDownParameters("genre", offset = Some(5))
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.offset=5")
     }
     "with drilldownOutputColumns" >> {
       val drilldown = DrillDownParameters("genre", outputColumns = Seq("_key", "_max"))
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.output_columns=_key%2C_max")
     }
     "with drilldownOutputColumns (no key, but key is to be add." >> {
       val drilldown = DrillDownParameters("genre", outputColumns = Seq("_max"))
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.output_columns=_max%2C_key")
     }
     "with drilldownLimit" >> {
       val drilldown = DrillDownParameters("genre", limit = Some(11))
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.limit=11")
     }
     "with drilldownCalcTypes" >> {
       val drilldown = DrillDownParameters("genre", calcTypes = Seq("MAX","MIN"))
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.calc_types=MAX%2CMIN")
     }
     "with drilldownCalcTarget" >> {
       val drilldown = DrillDownParameters("genre", calcTarget = Some("n_likes"))
       val command = new SelectParameters.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       command.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.calc_target=n_likes")
     }
   }
 }
