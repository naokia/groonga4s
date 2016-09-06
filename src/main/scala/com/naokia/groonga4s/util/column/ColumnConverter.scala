package com.naokia.groonga4s.util.column

trait ColumnConverter[T]{
  def convert(node: T): Any
}
