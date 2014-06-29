package controllers

import at.droelf.backend.service.GpxTrackService
import at.droelf.gui.entities.JsonSerializer
import org.joda.time.DateTimeZone
import play.api.mvc.{Action, Controller}

import scala.util.{Failure, Success, Try}

class TracksController(gpxTrackService: GpxTrackService) extends Controller with JsonSerializer {

  def uploadTracks(timezoneOffset: Int, activity: String) = Action(parse.raw) { implicit request =>
    val file = request.body.asFile

    val timeZone = Try(DateTimeZone.forOffsetHours(timezoneOffset))

    timeZone match {
      case Success(timeZone) => {
        gpxTrackService.saveTracks(file, activity, timeZone)
        Ok
      }
      case Failure(e) => BadRequest
    }
  }


}