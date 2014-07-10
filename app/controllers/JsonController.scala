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

        trackInformation match{
          case Some(data) => Ok(Json.toJson(DayTourInformationResponse(data._1,data._2,images)))
          case None => NotFound
        }
      }
      case Failure(e) => NotFound
    }
  }

}
