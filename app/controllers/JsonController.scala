package controllers

import at.droelf.backend.service.{ImageService, GpxTrackService}
import at.droelf.gui.entities.{DayTourInformationResponse, JsonSerializer}
import org.joda.time.LocalDate
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.util.{Failure, Success, Try}

class JsonController(gpxTrackService: GpxTrackService, imageService: ImageService) extends Controller with JsonSerializer{

  def getDayTourInformation(localDate: String) = Action{

    val parsedDate = Try(LocalDate.parse(localDate))

    parsedDate match {
      case Success(date) => {
        val trackInformation = gpxTrackService.getTrackInformationForLocalDate(date)
        val images = imageService.getImagesForDate(date)

        Ok(Json.toJson(DayTourInformationResponse(trackInformation._1,trackInformation._2,images)))

      }
      case Failure(e) => NotFound
    }
  }

}
