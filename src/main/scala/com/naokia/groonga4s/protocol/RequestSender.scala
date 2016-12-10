package com.naokia.groonga4s.protocol

import com.naokia.groonga4s.{Entity, RequestUri}
import com.naokia.groonga4s.command.{Command, CommandWithBody}
import com.naokia.groonga4s.response.{Response, ResponseParser}

/**
 * Interface for several protocol rappers (ex: HTTP, GQTP)
 */
trait RequestSender{
  def send(command: Command): (Entity, RequestUri)
  def sendWithBody(command: CommandWithBody): (Entity, RequestUri)
}
