package com.naokia.groonga4s.util.mapping

import org.specs2.mutable.Specification

case class PersonSimple(_key: String, mailAddress: String)
case class PersonWithSeq(_key: String, mailAddress: String, brother: Seq[String])
case class Job(name: String, year: Int, income: Int)
case class PersonWithJob(_key: String, mailAddress: String, job: Job)


class CollectionConverterSpec extends Specification {
  "CollectionConverter should" >> {
/*
    "convert Map to case class" >> {
      val personMap = Map("_key" -> "taro", "mail_address" -> "taro@example.com")
      val person = CollectionConverter.map2class[PersonSimple](classOf[PersonSimple], personMap)
      person must beAnInstanceOf[PersonSimple]
      person._key must beEqualTo("taro")
      person.mailAddress must beEqualTo("taro@example.com")
    }

    "convert Map with vector column to case class" >> {
      val personMap = Map("_key" -> "taro", "mail_address" -> "taro@example.com", "brother" -> Seq("jiro", "saburo"))
      val person = CollectionConverter.map2class[PersonWithSeq](classOf[PersonWithSeq], personMap)
      person.brother must beEqualTo(Seq("jiro", "saburo"))
    }
*/
    "convert nested Map with vector column to case class." >> {
      val personMap = Map("_key" -> "taro", "mail_address" -> "taro@example.com", "job" -> Job("engineer", 5, 600))
      val person = CollectionConverter.map2class[PersonWithJob](personMap)
      person.job must beEqualTo(Job("engineer", 5, 600))
    }
/*
    "convert Seq to case class" >> {
      case class Job(name: String, year: Int, income: Int)
      val jobSeq = Seq("engineer", 5, 600)
      val job = CollectionConverter.seq2class[Job](jobSeq, Job)
      job must beEqualTo(Job("engineer", 5, 600))
    }
    */
  }
}
