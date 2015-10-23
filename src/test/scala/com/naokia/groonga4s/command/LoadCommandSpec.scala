package com.naokia.groonga4s.command

import org.specs2.mutable.Specification

class LoadCommandSpec extends Specification{
  "LoadCommand" >> {
    "must execute load command to Groonga" >> {
      case class Person(_key: Int, mailAddress: String)
      val people = List[Person](Person(1, "john"))
      val parameters = LoadParameters[Person](classOf[Person], people, "Entities")
      val command = new LoadCommand(parameters)
      command.stringify must equalTo( """/d/load?table=Entities&columns=_key,mail_address""")
    }

    "must execute load command to Groonga (with ifexists parameter)" >> {
      case class Person(_key: Int, mailAddress: String)
      val people = List[Person](Person(1, "john"))
      val parameters = LoadParameters[Person](classOf[Person], people, "Entities", Some(true))
      val command = new LoadCommand(parameters)
      command.stringify must equalTo( """/d/load?table=Entities&columns=_key,mail_address&ifexists=true""")
    }

    "must execute load command to Groonga (without changing key names to snake case)" >> {
      case class Person(_key: Int, mailAddress: String)
      val people = List[Person](Person(1, "john"))
      val parameters = LoadParameters[Person](classOf[Person], people, "Entities", Some(true))
      val command = new LoadCommand(parameters, false)
      command.stringify must equalTo( """/d/load?table=Entities&columns=_key,mailAddress&ifexists=true""")
    }
  }
}
