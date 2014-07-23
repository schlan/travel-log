package controllers

import at.droelf.backend.ControllerUtils
import at.droelf.backend.service.{ImageService, TripService}
import play.api.mvc.{Action, Controller}

import scala.util.{Failure, Success, Try}

class TripController(tripService: TripService, imageService: ImageService) extends Controller with ControllerUtils {


  def getTripById(tripId: String) = Action {
    tripService.getTripById(tripId) match {
      case Some(trip) => Ok(views.html.tour.touroverview(trip.title, tripService.getAllTripsForNavBar, trip))
      case None => NotFound("getTripById: Err0r: Not found :(")
    }

  }

  def getDayTour(tripId: String, dayTourId: String) = Action {
    parseUUID(dayTourId,
      uuid => {
        (tripService.getTripById(tripId), tripService.getDayTourById(uuid)) match {
          case (Some(trip), Some(dayTour)) => Ok(views.html.tour.tour(trip.title + ": " +dayTour.date.toString, tripService.getAllTripsForNavBar(),trip, dayTour))
          case (_, _) => NotFound("getDayTour: Err0r: Not found :(")
        }
    })
  }
}
