package controllers

import at.droelf.gui.entities.JsonSerializer
import org.joda.time.LocalDate
import play.api.mvc.{Action, Controller}
import play.api.libs.json._
import java.util.UUID
import at.droelf.backend.service.GpxTrackService

class TracksController(gpxTrackService: GpxTrackService) extends Controller with JsonSerializer {


  def uploadTracks(name: String) = Action(parse.raw) { implicit request =>
    val file = request.body.asFile
    gpxTrackService.saveTracks(file)
    Ok(s"upload ok\n\n")
  }

  def getTrackPoints(id: String) = Action {
    gpxTrackService.getTrackById(UUID.fromString(id)) match{
      case Some(track) => Ok(Json.toJson(track.trackPoints.map(pt => Map(pt.dateTime.toString() -> Array(pt.longitude, pt.latitude)))))
      case None => Ok(Json.toJson(Array(0.0,0.0)))
    }
  }

  def getTrackElevation(id: String) = Action{
    gpxTrackService.getTrackById(UUID.fromString(id)) match{
      case Some(track) => Ok(Json.toJson(track.trackPoints.map(pt => Map(pt.dateTime.toString() -> pt.elevation))))
      case None => Ok(Json.toJson(Array(0.0,0.0)))
    }
  }

  def getAllTracks() = Action {
    //Ok(Json.toJson(gpxTrackService.getAllTracks().map(_.name)))
    Ok("")
  }

  def getTrackForLocalDate(localDate: String) = Action{
    try{
      val date = LocalDate.parse(localDate)

      Ok(Json.toJson(gpxTrackService.getTracksForLocalDate(date)))
    }catch {
      case e => NotFound("blub")
    }
  }
}