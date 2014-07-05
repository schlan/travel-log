package controllers

import java.util.UUID

import at.droelf.backend.service.{ImageService, TripService}
import play.api.mvc.{Action, Controller}

import scala.util.{Failure, Success, Try}

class TripController(tripService: TripService, imageService: ImageService) extends Controller  {


  def getTripById(tripId: String) = Action {
    tripService.getTripById(tripId) match {
      case Some(trip) => Ok(views.html.tour.touroverview("Tracks:", tripService.getAllTripsForNavBar, trip))
      case None => NotFound("getTripById: Err0r: Not found :(")
    }

  }

  def getDayTour(tripId: String, dayTourId: String) = Action {

      val uuid = Try(UUID.fromString(dayTourId))

      uuid match {
        case Success(dId) =>{
          (tripService.getTripById(tripId), tripService.getDayTour(tripId, dId)) match {
            case (Some(trip), Some(dayTour)) => {
              Ok(views.html.tour.tour("DayTour:", tripService.getAllTripsForNavBar(),trip, dayTour))
            }
            case (_, _) => NotFound("getDayTour: Err0r: Not found :(")
          }
        }
        case Failure(e) => NotFound
      }
  }



  def insertDemoTrip() = Action{
    tripService.insertDemoContent()
    Ok("Demo Trips Added")
  }

}
