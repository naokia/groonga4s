package com.naokia.groonga4s

/**
 * An exception class for GroongaClient
 */
case class GroongaException(code: Int, httpCode: Int, message: String, query: String) extends Exception(message)

