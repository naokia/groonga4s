package com.naokia.groonga4s.util.mapping

import org.specs2.mutable.Specification

class CollectionConverterSpec extends Specification {
  "CollectionConverter should" >> {
    "convert Map to case class" >> {
      case class Person(_key: String, mailAddress: String)
      val personMap = Map("_key" -> "taro", "mail_address" -> "taro@example.com")
      val person = CollectionConverter.map2class[Person](personMap, Person)
      person must beAnInstanceOf[Person]
      person._key must beEqualTo("taro")
      person.mailAddress must beEqualTo("taro@example.com")
    }

    "convert Map with vector column to case class" >> {
      case class Person(_key: String, mailAddress: String, brother: Seq[String])
      val personMap = Map("_key" -> "taro", "mail_address" -> "taro@example.com", "brother" -> Seq("jiro", "saburo"))
      val person = CollectionConverter.map2class[Person](personMap, Person)
      person.brother must beEqualTo(Seq("jiro", "saburo"))
    }

    "convert nested Map with vector column to case class." >> {
      case class Job(name: String, year: Int, income: Int)
      case class Person(_key: String, mailAddress: String, job: Job)
      val personMap = Map("_key" -> "taro", "mail_address" -> "taro@example.com", "job" -> Job("engineer", 5, 600))
      val person = CollectionConverter.map2class[Person](personMap, Person)
      person.job must beEqualTo(Job("engineer", 5, 600))
    }

    "convert Seq to case class" >> {
      case class Job(name: String, year: Int, income: Int)
      val jobSeq = Seq("engineer", 5, 600)
      val job = CollectionConverter.seq2class[Job](jobSeq, Job)
      job must beEqualTo(Job("engineer", 5, 600))
    }
  }
}
