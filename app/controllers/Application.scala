package controllers

import play.api.mvc._
import at.droelf.backend.service.GpxTrackService

class Application(gpxTrackService: GpxTrackService)extends Controller {


  def index = Action {
    Redirect(routes.TripController.getTripById("pacific"))
  }

}
