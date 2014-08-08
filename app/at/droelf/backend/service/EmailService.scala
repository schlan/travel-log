package at.droelf.backend.service

import at.droelf.Config
import at.droelf.gui.entities.Email
import org.joda.time.DateTime
import play.api.Play.current
import com.typesafe.plugin._

class EmailService {


  def sendMail(email: Email)= {

    val mail = use[MailerPlugin].email

    mail.setSubject("Message from droelf.at")
    mail.setRecipient(Config.emailRecipient.get)
    mail.setFrom(Config.emailSender.get)
    mail.sendHtml(generateBody(email))


  }

  private def generateBody(email: Email): String = {
    s"<html>From: ${email.from}<br>Date: ${DateTime.now}<br><br><hr><br>Message:<br>${email.text}<br><br><hr><br><br>Kind regards<br>internet</html>"
  }


}
