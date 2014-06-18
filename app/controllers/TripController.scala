package controllers

import java.util.UUID

import at.droelf.backend.service.TripService
import play.api.mvc.{Action, Controller}

class TripController(tripService: TripService) extends Controller  {


  def getTripById(tripId: String) = Action {
    tripService.getTripById(tripId) match {
      case Some(x) => Ok(views.html.touroverview("Tracks:", x))
      case None => NotFound("getTripById: Err0r: Not found :(")
    }

  }

  def getDayTour(tripId: String, dayTourId: String) = Action {
    try {
      val dId = UUID.fromString(dayTourId)

      (tripService.getTripById(tripId), tripService.getDayTour(tripId, dId)) match {
        case (Some(trip), Some(dayTour)) => Ok(views.html.tour("DayTour:", trip, dayTour))
        case (_, _) => NotFound("getDayTour: Err0r: Not found :(")
      }

    } catch {
      case e: IllegalArgumentException => NotFound("getDayTour: Err0r: Not found :(")
    }
  }

  def insertDemoTrip() = Action{
    tripService.insertDemoContent()
    Ok("Demo Trips Added")
  }

}
