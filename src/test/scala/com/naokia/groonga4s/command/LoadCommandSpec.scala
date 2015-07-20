package com.naokia.groonga4s.command

import org.specs2.mutable.Specification

class LoadCommandSpec extends Specification{
  "LoadCommand" >> {
    "must execute load command to Groonga" >>{
      val command = new LoadCommand("[[\"_key\", \"name\"], [\"1\", \"john\"]]", "Entities", List("name", "age"))
      command.stringify must equalTo("/d/load?values=[[\"_key\", \"name\"], [\"1\", \"john\"]]&table=Entities&columns=name,age")
    }
    "must execute load command to Groonga (with ifexists parameter)" >>{
      val command = new LoadCommand("[[\"_key\", \"name\"], [\"1\", \"john\"]]", "Entities", List("name", "age"), Some("true"))
      command.stringify must equalTo("/d/load?values=[[\"_key\", \"name\"], [\"1\", \"john\"]]&table=Entities&columns=name,age&ifexists=true")
    }
  }
}
