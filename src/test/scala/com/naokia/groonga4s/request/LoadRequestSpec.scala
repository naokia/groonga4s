package com.naokia.groonga4s.request

import org.specs2.mutable.Specification

class LoadRequestSpec extends Specification{
  "LoadRequest" >> {
    "executes load command to Groonga" >> {
      case class Person(_key: Int, mailAddress: String)
      val people = List[Person](Person(1, "john"))
      val request = new LoadRequest[Person]("Entities", classOf[Person], people)
      request.toQuery must equalTo( """/d/load?table=Entities&columns=_key,mail_address""")
    }

    "executes load command to Groonga (with ifexists parameter)" >> {
      case class Person(_key: Int, mailAddress: String)
      val people = List[Person](Person(1, "john"))
      val request = new LoadRequest[Person]("Entities", classOf[Person], people, Some(true))
      request.toQuery must equalTo( """/d/load?table=Entities&columns=_key,mail_address&ifexists=true""")
    }

    "executes load command to Groonga (without changing key names to snake case)" >> {
      case class Person(_key: Int, mailAddress: String)
      val people = List[Person](Person(1, "john"))
      val request = new LoadRequest[Person]("Entities", classOf[Person], people, Some(true))
      request.toQuery must equalTo( """/d/load?table=Entities&columns=_key,mail_address&ifexists=true""")
    }

    "generate data for POST method " >> {
      case class Person(_key: Int, mailAddress: String)
      val people = List[Person](Person(1, "john"), Person(2, "mary"))
      val request = new LoadRequest[Person]( "Entities", classOf[Person], people,Some(true))
      request.getBody must equalTo( """[{"_key":1,"mail_address":"john"},{"_key":2,"mail_address":"mary"}]""")
    }

    "convert case class with tuple to JSON. tuple is converted to vector column" >> {
      case class Person(_key: Int, mailAddress: String, job: (String, Int, String))
      val people = List(Person(1, "taro@example.com", ("engineer", 5, "tokyo")))
      val request = new LoadRequest[Person]("Entities", classOf[Person], people)
      request.getBody must equalTo( """[{"_key":1,"mail_address":"taro@example.com","job":["engineer",5,"tokyo"]}]""")
    }

    "convert case class with array to JSON. array is converted to vector column" >> {
      case class Person(_key: Int, mailAddress: String, brother: List[String])
      val people = List(Person(1, "taro@example.com", List("jiro", "saburo")))
      val request = new LoadRequest[Person]("Entities", classOf[Person], people)
      request.getBody must equalTo( """[{"_key":1,"mail_address":"taro@example.com","brother":["jiro","saburo"]}]""")

    }
  }
}
