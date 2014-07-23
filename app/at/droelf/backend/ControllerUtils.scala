package at.droelf.backend

import java.util.UUID

import play.api.mvc.{Results, SimpleResult}

import scala.util.{Failure, Success, Try}


trait ControllerUtils {

  def parseUUID(id: String, success: UUID => SimpleResult, failure: Throwable => SimpleResult = (t => Results.NotFound(t.getMessage()))): SimpleResult = {
    Try(UUID.fromString(id)) match {
      case Success(uuid) => success(uuid)
      case Failure(e) => failure(e)
    }
  }

}
