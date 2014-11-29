package controllers

import at.droelf.backend.service.UserService
import play.api.mvc.{Security, Action, Controller}
import views.html
import play.api.data.Forms._
import play.api.data.Form


class AuthController(userService: UserService) extends Controller{

  val loginForm = Form(
    tuple(
      "username" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => userService.checkUserPassword(email, password)
    })
  )

  def login = Action { implicit request =>
    request.session.get(Security.username).map { user =>
      Redirect(routes.AdminController.index())
    }.getOrElse {
      Ok(html.admin.login(loginForm))
    }
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.admin.login(formWithErrors)),
      user => Redirect(routes.AdminController.index).withSession(Security.username -> user._1)
    )
  }

  def logout = Action {
    Redirect(routes.HomeController.index).withNewSession
  }

}
