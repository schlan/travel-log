package controllers

import play.api.mvc.{Result, Results, Action, Controller}
import java.util.UUID
import at.droelf.backend.service.ImageService
import org.joda.time.LocalDate
import org.joda.time.format.{ISODateTimeFormat, DateTimeFormat, DateTimeFormatter}
import scala.util.{Failure, Success, Try}

class ImageController(imageService: ImageService) extends Controller {
  def uploadImage(date: String, name: String) = Action(parse.raw) { implicit request =>

    val problem = Try(ISODateTimeFormat.dateTimeNoMillis().parseDateTime(date))

    problem match {
      case Success(v) => {
        val file = request.body.asFile
        imageService.saveImage(file, date, name)
        Ok
      }
      case Failure(e) => BadRequest
    }
  }
}
