package com.naokia.groonga4s.util.mapping

import com.naokia.groonga4s.response.Person
import org.specs2.mutable.Specification

import scala.reflect.runtime.universe._
import scala.reflect.{ClassTag, classTag}

case class PersonSimple(_key: String, mailAddress: String)
case class PersonWithSeq(_key: String, mailAddress: String, brother: Seq[String])
case class Job(name: String, year: Int, income: Int)
case class PersonWithJob(_key: String, mailAddress: String, job: Job)

class CollectionConverterSpec extends Specification {
  "CollectionConverter should" >> {
    "convert Map to case class" >> {
      val personMap = Map("_key" -> "taro", "mail_address" -> "taro@example.com")
      val tt = typeTag[PersonSimple]
      val ct = classTag[PersonSimple]
      val person = CollectionConverter.map2class[PersonSimple](personMap)
      person must beAnInstanceOf[PersonSimple]
      person._key must beEqualTo("taro")
      person.mailAddress must beEqualTo("taro@example.com")
    }

    "convert Map with vector column to case class" >> {
      val personMap = Map("_key" -> "taro", "mail_address" -> "taro@example.com", "brother" -> Seq("jiro", "saburo"))
      val tt = typeTag[PersonWithSeq]
      val ct = classTag[PersonWithSeq]
      val person = CollectionConverter.map2class[PersonWithSeq](personMap)
      person.brother must beEqualTo(Seq("jiro", "saburo"))
    }
    "convert nested Map with vector column to case class." >> {
      val personMap = Map("_key" -> "taro", "mail_address" -> "taro@example.com", "job" -> Job("engineer", 5, 600))
      val tt = typeTag[PersonWithJob]
      val ct = classTag[PersonWithJob]
      val person = CollectionConverter.map2class[PersonWithJob](personMap)
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
