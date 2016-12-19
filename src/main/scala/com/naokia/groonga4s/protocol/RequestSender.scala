package com.naokia.groonga4s.protocol

import com.naokia.groonga4s.{Entity, RequestUri}
import com.naokia.groonga4s.request.{Request, RequestWithBody}

/**
 * Interface for several protocol rappers (ex: HTTP, GQTP)
 */
trait RequestSender{
  def send(command: Request): (Entity, RequestUri)
  def sendWithBody(command: RequestWithBody): (Entity, RequestUri)
}
