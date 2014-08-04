package controllers

import play.api.mvc._
import scala.slick.lifted.TableQuery
import at.droelf.backend.service.GpxTrackService
import models.DayTourTable


class Application(gpxTrackService: GpxTrackService)extends Controller {


  def index = Action {
    Redirect(routes.TripController.getTripById("pacific"))
  }

}
