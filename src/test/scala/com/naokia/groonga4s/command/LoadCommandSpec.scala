package com.naokia.groonga4s.command

import org.specs2.mutable.Specification

class LoadCommandSpec extends Specification{
  "LoadCommand" >> {
    "executes load command to Groonga" >> {
      case class Person(_key: Int, mailAddress: String)
      val people = List[Person](Person(1, "john"))
      val parameters = LoadParameters[Person](classOf[Person], people, "Entities")
      val command = new LoadCommand(parameters)
      command.getQuery must equalTo( """/d/load?table=Entities&columns=_key,mail_address""")
    }

    "executes load command to Groonga (with ifexists parameter)" >> {
      case class Person(_key: Int, mailAddress: String)
      val people = List[Person](Person(1, "john"))
      val parameters = LoadParameters[Person](classOf[Person], people, "Entities", Some(true))
      val command = new LoadCommand(parameters)
      command.getQuery must equalTo( """/d/load?table=Entities&columns=_key,mail_address&ifexists=true""")
    }

    "executes load command to Groonga (without changing key names to snake case)" >> {
      case class Person(_key: Int, mailAddress: String)
      val people = List[Person](Person(1, "john"))
      val parameters = LoadParameters[Person](classOf[Person], people, "Entities", Some(true))
      val command = new LoadCommand(parameters, false)
      command.getQuery must equalTo( """/d/load?table=Entities&columns=_key,mailAddress&ifexists=true""")
    }

    "generate data for POST method " >> {
      case class Person(_key: Int, mailAddress: String)
      val people = List[Person](Person(1, "john"), Person(2, "mary"))
      val parameters = LoadParameters[Person](classOf[Person], people, "Entities", Some(true))
      val command = new LoadCommand(parameters)
      command.getBody must equalTo( """[{"_key":1,"mail_address":"john"},{"_key":2,"mail_address":"mary"}]""")
    }

    "convert case class with tuple to JSON. tuple is converted to vector column" >> {
      case class Person(_key: Int, mailAddress: String, job: (String, Int, String))
      val people = List(Person(1, "taro@example.com", ("engineer", 5, "tokyo")))
      val command = new LoadCommand(LoadParameters[Person](classOf[Person], people, "Entities"))
      command.getBody must equalTo( """[{"_key":1,"mail_address":"taro@example.com","job":["engineer",5,"tokyo"]}]""")
    }

    "convert case class with array to JSON. array is converted to vector column" >> {
      case class Person(_key: Int, mailAddress: String, brother: List[String])
      val people = List(Person(1, "taro@example.com", List("jiro", "saburo")))
      val command = new LoadCommand(LoadParameters[Person](classOf[Person], people, "Entities"))
      command.getBody must equalTo( """[{"_key":1,"mail_address":"taro@example.com","brother":["jiro","saburo"]}]""")
    }
  }
}
