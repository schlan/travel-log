package controllers

import at.droelf.backend.service.{ImageService, GpxTrackService}
import at.droelf.gui.entities.{DayTourInformationResponse, JsonSerializer}
import org.joda.time.LocalDate
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

class JsonController(gpxTrackService: GpxTrackService, imageService: ImageService) extends Controller with JsonSerializer{

  def getDayTourInformation(localDate: String) = Action{
    try{
      val date = LocalDate.parse(localDate)

      val trackInformation = gpxTrackService.getTrackInformationForLocalDate(date)
      val images = imageService.getImagesForDate(date)

      Ok(Json.toJson(DayTourInformationResponse(trackInformation._1,trackInformation._2,images)))
    }catch {
      case e: Throwable=> NotFound
    }
  }


}
