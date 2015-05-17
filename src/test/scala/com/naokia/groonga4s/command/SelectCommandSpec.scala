package com.naokia.groonga4s.command

import org.specs2.mutable.Specification

/**
  * Created by naoki on 15/04/29.
  */
class SelectCommandSpec extends Specification{
   "SelectCommand" >> {
     "must generate path and query from table name" >>  {
       val parameters = SelectParameters(table="Entries")

       val command = new SelectCommand(parameters)
       command.stringify() must equalTo("/d/select.json?table=Entries")
     }
     "must generate path and query from table name and some parameters" >> {
       val parameters = SelectParameters(table="Entries", query=Some("name:John"))

       val command = new SelectCommand(parameters)
       command.stringify() must equalTo("/d/select.json?table=Entries&query=name%3AJohn")
     }
   }
 }
