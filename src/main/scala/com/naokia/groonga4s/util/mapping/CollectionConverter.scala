package com.naokia.groonga4s.util.mapping

/**
 * convert some collection to case class
 */
object CollectionConverter {
  /**
   * convert Map to case class
   *
   * TODO: don't use Any
   *
   * @param map Map
   * @param clazz case class
   * @tparam T type parameter of clazz
   * @return
   */
  def map2class[T](map: Map[String, Any], clazz: Any): T = {
    val apply = clazz.asInstanceOf[T].getClass.getMethods.find(_.getName == "apply")
    apply.get.invoke(clazz, map.values.toList.map(_.asInstanceOf[AnyRef]):_*).asInstanceOf[T]
  }

  /**
   * convert Seq to case class
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
}
