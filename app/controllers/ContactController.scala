package controllers

import at.droelf.Config
import at.droelf.backend.service.{TripService, EmailService}
import at.droelf.gui.entities.Email
import org.joda.time.{Minutes, DateTime}
import play.api.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._

class ContactController(emailService: EmailService, tripService: TripService) extends Controller{

  val sessionKey = "messageSend"

  def sendMessage = Action { implicit request =>

    def parseFormAndSendMessage()(implicit request: Request[_]): SimpleResult = {
      ContactAssets.contactForm.bindFromRequest().fold(
          formWithErrors => BadRequest("Check the form"),
        email => {
          emailService.sendMail(email)
          Redirect(routes.Application.index).withSession(sessionKey -> DateTime.now().toString())
        }
      )
    }

    request.session.get(sessionKey) match {
      case Some(timeStamp) => {
        val diff = Math.abs(Minutes.minutesBetween(DateTime.parse(timeStamp), DateTime.now).getMinutes)
        Logger.info(s"Last mail sent: ${diff}")
        if(diff >= Config.emailHold.get){
          parseFormAndSendMessage()(request)
        }else{
          BadRequest(s"Please wait ${diff.toString} minutes")
        }
      }
      case None => {
        parseFormAndSendMessage()(request)
      }
    }
  }
}

object ContactAssets{
  val contactForm = Form(
    mapping(
      "From:" -> nonEmptyText,
      "Message:" -> nonEmptyText
    )(Email.apply)(Email.unapply)
  )
}