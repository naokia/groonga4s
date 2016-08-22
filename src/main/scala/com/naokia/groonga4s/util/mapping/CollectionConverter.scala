package com.naokia.groonga4s.util.mapping

import com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase
import com.naokia.groonga4s.util.column.SimpleLowerCaseWithUnderscoresStrategy
import scala.reflect.{ClassTag, classTag}
import scala.reflect.runtime.universe._

/**
 * Converts some collection to case class
 */
object CollectionConverter {
  /**
   * Converts Map to case class
   *
   * @param map Map
   * @return
   */
  def map2class[T: TypeTag: ClassTag](map: Map[String, _], propertyNamingStrategy: Option[PropertyNamingStrategyBase] = Some(SimpleLowerCaseWithUnderscoresStrategy)): T = {
    val (constructorMirror, paramLists) = reflectConstructor[T]()

    val constructorArgs = paramLists.map( (param:Symbol) => {
      val paramName = applyNamingStrategy(param.name.toString, propertyNamingStrategy)

      if(param.typeSignature <:< typeOf[Option[Any]])
        map.get(paramName)
      else
        map.getOrElse(paramName, throw new IllegalArgumentException("Map is missing required parameter named " + paramName))
    })

    constructorMirror(constructorArgs:_*).asInstanceOf[T]
  }

  /**
    * Get name of properties
    *
    * @param clazz
    * @param propertyNamingStrategy
    * @tparam T
    * @return
    */
  def getPropertyNames[T](clazz: Class[T], propertyNamingStrategy: Option[PropertyNamingStrategyBase] = Some(SimpleLowerCaseWithUnderscoresStrategy)): Seq[String] = {
    clazz.getDeclaredFields.map(_.getName).filter(!_.startsWith("$")).map{name => applyNamingStrategy(name, propertyNamingStrategy)}.toSeq
  }

  private def applyNamingStrategy(name: String, propertyNamingStrategy: Option[PropertyNamingStrategyBase] = Some(SimpleLowerCaseWithUnderscoresStrategy)): String = {
    propertyNamingStrategy match {
      case Some(propertyNamingStrategy) => propertyNamingStrategy.translate(name)
      case _ => name.toString
    }
  }

  private def reflectConstructor[T: TypeTag: ClassTag](rm: Option[Mirror] = None): (MethodMirror, List[Symbol])= {
    val classTest = typeOf[T].typeSymbol.asClass
    val classLoader = classTag[T].runtimeClass.getClassLoader
    val classMirror = (rm match {
      case Some(rm) => rm
      case None => runtimeMirror(classLoader)

    }).reflectClass(classTest)
    val constructor = typeOf[T].decl(termNames.CONSTRUCTOR).asMethod
    (classMirror.reflectConstructor(constructor), constructor.paramLists.flatten)
  }

  /**
   * Converts Seq to case class
   *
   * TODO: don't use Any
   *
   * @param seq Seq
   * @param clazz case class
   * @tparam T type parameter of clazz
   * @return
   */
  def seq2class[T](seq: Seq[Any], clazz: Any): T = {
    val apply = clazz.asInstanceOf[T].getClass.getMethods.find(_.getName == "apply")
    apply.get.invoke(clazz, seq.map(_.asInstanceOf[AnyRef]):_*).asInstanceOf[T]
  }

  def applyMethods[T](clazz: Class[T]): Option[java.lang.reflect.Method] = {
    val companionObj = Class.forName(clazz.getName + "$")

    companionObj.getMethods.find(_.getName == "apply")
  }
}
