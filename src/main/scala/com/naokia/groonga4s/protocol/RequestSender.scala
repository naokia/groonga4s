package com.naokia.groonga4s.protocol

import com.naokia.groonga4s.command.{Command, CommandWithBody}
import com.naokia.groonga4s.response.{Response, ResponseParser}

import scala.util.Try

/**
 * Interface for several protocol rappers (ex: HTTP, GQTP)
 */
trait RequestSender{
  def send[T <: Response](command: Command, parser: ResponseParser[T]): Try[T]
  def sendWithBody[T <: Response](command: CommandWithBody, parser: ResponseParser[T]): Try[T]
}
