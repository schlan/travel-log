package controllers

import java.nio.file.Files

import at.droelf.backend.BasicAuth
import at.droelf.backend.service.{ImageService, UserService}
import org.joda.time.format.ISODateTimeFormat
import play.api.Logger
import play.api.mvc.{Action, Controller}

import scala.util.{Failure, Success, Try}

class ImageController(imageService: ImageService, userService: UserService) extends Controller with BasicAuth {


  def uploadImage() = Action(parse.multipartFormData) { implicit request =>

    request.headers.get("Authorization").map {
      basicAuth =>
        val (user, pass) = decodeBasicAuth(basicAuth)

        if (userService.checkUserPassword(user, pass)) {

          val parsedDateTimeTry = Try(ISODateTimeFormat.dateTimeNoMillis().withOffsetParsed().parseDateTime(request.body.asFormUrlEncoded.get("dateTime").get.head))

          parsedDateTimeTry match {
            case Success(parsedDateTime) => {

              request.body.file("file").map(tmpFile => {
                imageService.saveImage(tmpFile.ref, parsedDateTime, request.body.asFormUrlEncoded.get("name").get.head)
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
    println(image.canRead)
    Ok.sendFile(
      content = image,
      inline = true
    ).withHeaders(CONTENT_TYPE -> "image/jpg")
  }

}
