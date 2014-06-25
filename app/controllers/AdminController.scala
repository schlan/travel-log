package controllers

import play.api.mvc.{Action, Controller}

class AdminController extends Controller{

  def index = Action{ implicit request =>
    Ok(views.html.admin.adminarea("Admin"))
  }


}
