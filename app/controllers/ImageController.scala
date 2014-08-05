package controllers

import java.nio.file.Files

import at.droelf.backend.BasicAuth
import at.droelf.backend.service.{ImageService, UserService}
import org.joda.time.format.ISODateTimeFormat
import play.api.mvc.{Action, Controller}

import scala.util.{Failure, Success, Try}

class ImageController(imageService: ImageService, userService: UserService) extends Controller with BasicAuth {


  def uploadImage(date: String, name: String) = Action(parse.multipartFormData) { implicit request =>

    request.headers.get("Authorization").map {
      basicAuth =>
        val (user, pass) = decodeBasicAuth(basicAuth)

        if (userService.checkUserPassword(user, pass)) {

          val parsedDateTimeTry = Try(ISODateTimeFormat.dateTimeNoMillis().withOffsetParsed().parseDateTime(date))

          parsedDateTimeTry match {
            case Success(parsedDateTime) => {

              request.body.file("file").map(tmpFile => {
                imageService.saveImage(tmpFile.ref, parsedDateTime, name)
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

  def getImage(name: String) = Action { implicit request =>
    val image = imageService.getImageFile(name)
    Ok.sendFile(
      content = image,
      inline = true
    ).withHeaders(CONTENT_TYPE -> Files.probeContentType(image.toPath))
  }

}
