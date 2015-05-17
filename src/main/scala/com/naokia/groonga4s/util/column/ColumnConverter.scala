package com.naokia.groonga4s.util.column

/**
 * Created by naoki on 15/05/16.
 */
trait ColumnConverter[T]{
  def convert(node: T): Any
}
