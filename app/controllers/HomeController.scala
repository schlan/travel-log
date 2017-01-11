package controllers

import play.api.mvc._
import at.droelf.backend.service.GpxTrackService

class HomeController(gpxTrackService: GpxTrackService) extends Controller {

  def index = Action {
    Redirect("/pacific")
  }

}
