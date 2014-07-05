package controllers

import java.nio.file.Files

import play.api.mvc.{Result, Results, Action, Controller}
import java.util.UUID
import at.droelf.backend.service.{UserService, ImageService}
import org.joda.time.LocalDate
import org.joda.time.format.{ISODateTimeFormat, DateTimeFormat, DateTimeFormatter}
import scala.util.{Failure, Success, Try}

class ImageController(imageService: ImageService, userService: UserService) extends Controller {


  def uploadImage(date: String, name: String) = Action(parse.raw) { implicit request =>

    def decodeBasicAuth(auth: String) = {
      val baStr = auth.replaceFirst("Basic ", "")
      val Array(user, pass) = new String(new sun.misc.BASE64Decoder().decodeBuffer(baStr), "UTF-8").split(":")
      (user, pass)
    }

    request.headers.get("Authorization").map{
      basicAuth =>
        val (user, pass) = decodeBasicAuth(basicAuth)
        if(userService.checkUserPassword(user,pass)){

          val problem = Try(ISODateTimeFormat.dateTimeNoMillis().parseDateTime(date))

          problem match {
            case Success(v) => {
              val file = request.body.asFile
              imageService.saveImage(file, v, name)
              Ok
            }
            case Failure(e) => BadRequest
          }

        }else{
          Unauthorized

        }

        Ok(s"Hello $user identified by $pass")
    }.getOrElse(Unauthorized)

  }

  trait BasicAuth{


  }

  def getImage(name: String) = Action{ implicit request =>
      val image = imageService.getImageFile(name)
      Ok.sendFile(
        content = image,
        inline = true
      ).withHeaders(CONTENT_TYPE -> Files.probeContentType(image.toPath))
  }

}
