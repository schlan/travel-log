package controllers

import at.droelf.backend.Secured
import at.droelf.backend.service.UserService
import play.api.mvc.{Security, Action, Controller}

class AdminController(userSer: UserService) extends Controller with Secured{

  override val userService: UserService = userSer

  def index = withAuth{ username => implicit request =>
      Ok(views.html.admin.adminarea(username))
  }


}
