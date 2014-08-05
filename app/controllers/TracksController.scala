package controllers

import at.droelf.backend.BasicAuth
import at.droelf.backend.service.{UserService, GpxTrackService}
import at.droelf.gui.entities.JsonSerializer
import org.joda.time.DateTimeZone
import play.api.mvc.{Action, Controller}

import scala.util.{Failure, Success, Try}

class TracksController(gpxTrackService: GpxTrackService, userService: UserService) extends Controller with JsonSerializer with BasicAuth {

  def uploadTracks(timezoneOffset: Int, activity: String) = Action(parse.multipartFormData) { implicit request =>

    request.headers.get("Authorization").map {
      basicAuth =>
        val (user, pass) = decodeBasicAuth(basicAuth)

        if (userService.checkUserPassword(user, pass)) {
          val timeZone = Try(DateTimeZone.forOffsetHours(timezoneOffset))

          timeZone match {
            case Success(timeZone) => {
              request.body.file("file").map( tmpFile => {
                gpxTrackService.saveTracks(tmpFile.ref, activity, timeZone)
                Ok
              }).getOrElse(BadRequest)
            }
            case Failure(e) => BadRequest
          }
        }else{
          Unauthorized
        }

    }.getOrElse(Unauthorized)
  }


}