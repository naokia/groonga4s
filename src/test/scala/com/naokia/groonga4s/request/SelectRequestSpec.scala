package com.naokia.groonga4s.request

import org.specs2.mutable.Specification

class SelectRequestSpec extends Specification{
  case class Entry( _id: Int, _key: String, _score: Int)

   "SelectRequest generates path and query" >> {
     "with table name only" >>  {
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score")
     }

     "with query and matchColumns" >> {
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withQuery("John")
         .withMatchColumns(Seq("name"))
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&query=John&match_columns=name")
     }
     "with filter" >> {
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withFilter("n_likes >= 5").build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&filter=n_likes+%3E%3D+5")
     }
     "with sortBy" >> {
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withSortBy(Seq("_id"))
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&sortby=_id")
     }
     "with scorer" >> {
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withScorer("_score = rand()")
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&scorer=%27_score+%3D+rand%28%29%27")
     }
     "with offset" >> {
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withOffset(10)
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&offset=10")
     }
     "with limit" >> {
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withLimit(15)
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&limit=15")
     }
     "with cache" >> {
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withCache(true)
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&cache=yes")
     }
     "with no cache" >> {
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withCache(false)
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&cache=no")
     }
     "with matchEscalationThreshold" >> {
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withQuery("John")
         .withMatchColumns(Seq("name"))
         .withMatchEscalationThreshold(-1)
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&query=John&match_columns=name&match_escalation_threshold=-1")
     }
     "with queryFlags" >> {
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withQuery("John")
         .withMatchColumns(Seq("name"))
         .withQueryFlags("ALLOW_PRAGMA|ALLOW_COLUMN|ALLOW_UPDATE|ALLOW_LEADING_NOT|NONE")
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&query=John&match_columns=name&query_flags=ALLOW_PRAGMA%7CALLOW_COLUMN%7CALLOW_UPDATE%7CALLOW_LEADING_NOT%7CNONE")
     }
     "with queryExpander" >> {
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withQuery("John")
         .withMatchColumns(Seq("name"))
         .withQueryExpander("Thesaurus.synonym")
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&query=John&match_columns=name&query_expander=Thesaurus.synonym")
     }
     "with adjuster" >> {
       val request = new SelectRequest.Builder("Entries")
         .withAdjuster("""content @ "groonga" * 5""")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&adjuster=content+%40+%22groonga%22+*+5")
     }
     "with drilldown" >> {
       val drilldown = DrillDown("genre")
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre")
     }
     "with drilldownSortby" >> {
       val drilldown = DrillDown("genre", sortby = Seq("-_nsubrecs", "_key"))
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.sortby=-_nsubrecs%2C_key")
     }
     "with drilldownOffset" >> {
       val drilldown = DrillDown("genre", offset = Some(5))
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.offset=5")
     }
     "with drilldownOutputColumns" >> {
       val drilldown = DrillDown("genre", outputColumns = Seq("_key", "_max"))
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.output_columns=_key%2C_max")
     }
     "with drilldownOutputColumns (no key, but key is to be add." >> {
       val drilldown = DrillDown("genre", outputColumns = Seq("_max"))
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.output_columns=_max%2C_key")
     }
     "with drilldownLimit" >> {
       val drilldown = DrillDown("genre", limit = Some(11))
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.limit=11")
     }
     "with drilldownCalcTypes" >> {
       val drilldown = DrillDown("genre", calcTypes = Seq("MAX","MIN"))
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.calc_types=MAX%2CMIN")
     }
     "with drilldownCalcTarget" >> {
       val drilldown = DrillDown("genre", calcTarget = Some("n_likes"))
       val request = new SelectRequest.Builder("Entries")
         .withOutputColumns(Seq("_id", "_key", "_score"))
         .withDrillDowns(Seq(drilldown))
         .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&drilldown%5Bgenre%5D.keys=genre&drilldown%5Bgenre%5D.calc_target=n_likes")
     }
     "with option" >> {
       val request = new SelectRequest.Builder("Entries")
           .withOutputColumns(Seq("_id", "_key", "_score"))
           .withOption("key1", "value1")
           .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&key1=value1")
     }
     "with multiple option" >> {
       val request = new SelectRequest.Builder("Entries")
           .withOutputColumns(Seq("_id", "_key", "_score"))
           .withOption("key1", "value1")
           .withOption("key2", "value2")
           .build
       request.toQuery must equalTo("/d/select.json?table=Entries&output_columns=_id%2C_key%2C_score&key1=value1&key2=value2")
     }
   }
 }
