package controllers

import at.droelf.backend.ControllerUtils
import at.droelf.backend.service.{ImageService, TripService}
import at.droelf.gui.entities.GuiDayTour
import controllers.ContactAssets
import models.DayTour
import org.joda.time.LocalDate
import play.api.mvc.{Action, Controller}

import scala.util.{Failure, Success, Try}

class TripController(tripService: TripService, imageService: ImageService) extends Controller with ControllerUtils {


  def getTripById(tripId: String) = Action { implicit request =>
    tripService.getTripById(tripId) match {
      case Some(trip) => Ok(views.html.tour.touroverview(trip.title, tripService.getAllTripsForNavBar,  trip, getCategoryToDayTourMapping(trip.guiDayTours),ContactAssets.contactForm))
      case None => NotFound("getTripById: Err0r: Not found :(")
    }

  }

  def getDayTour(tripId: String, dayTourId: String) = Action { implicit request =>
    parseUUID(dayTourId,
      uuid => {
        (tripService.getTripById(tripId), tripService.getDayTourById(uuid)) match {
          case (Some(trip), Some(dayTour)) => Ok(views.html.tour.tour(trip.title + ": " +dayTour.date.toString, tripService.getAllTripsForNavBar(),trip, dayTour,getCategoryToDayTourMapping(trip.guiDayTours),ContactAssets.contactForm))
          case (_, _) => Redirect(routes.TripController.getTripById(tripId))
        }
    })
  }

  def getCategoryToDayTourMapping(dayTours: Seq[GuiDayTour]): Map[(String, LocalDate), Seq[(GuiDayTour, Int)]] = {
    def sort(dt: Seq[GuiDayTour]) = dt.sortWith((d1,d2) => d1.date.isBefore(d2.date))
    def sortWI(dt: Seq[(GuiDayTour, Int)]) = dt.sortWith((d1,d2) => d1._1.date.isAfter(d2._1.date))
    val dt = sort(dayTours).zipWithIndex.map( row => (row._1, row._2 + 1))
    dt.groupBy(_._1.category).map(row => ( (row._1, sortWI(row._2).head._1.date) -> sortWI(row._2) ))
  }
}
