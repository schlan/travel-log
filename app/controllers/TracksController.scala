package controllers

import at.droelf.backend.BasicAuth
import at.droelf.backend.service.{GpxTrackService, UserService}
import at.droelf.gui.entities.JsonSerializer
import org.joda.time.DateTimeZone
import play.api.Logger
import play.api.mvc.{Action, Controller}

import scala.util.{Failure, Success, Try}

class TracksController(gpxTrackService: GpxTrackService, userService: UserService) extends Controller with JsonSerializer with BasicAuth {

  def uploadTracks() = Action(parse.multipartFormData) { implicit request =>

    request.headers.get("Authorization").map {
      basicAuth =>
        val (user, pass) = decodeBasicAuth(basicAuth)

        if (userService.checkUserPassword(user, pass)) {
          val timeZone = Try(DateTimeZone.forID(request.body.asFormUrlEncoded.get("timeZone").get.head))

          timeZone match {
            case Success(timeZone) => {
              request.body.file("file").map(tmpFile => {
                gpxTrackService.saveTracks(tmpFile.ref, request.body.asFormUrlEncoded.get("activity").get.head, timeZone)
                Ok
              }).getOrElse(BadRequest)
            }
            case Failure(e) => BadRequest
          }
        } else {
          Unauthorized
        }

    }.getOrElse(Unauthorized)
  }


}