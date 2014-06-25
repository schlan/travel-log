package controllers

import java.util.UUID

import at.droelf.backend.Secured
import at.droelf.backend.service.{AdminService, UserService}
import models.{DayTour, DayTourMetaData, Trip}
import org.joda.time.LocalDate
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.Controller

class AdminController(userSer: UserService, adminService: AdminService) extends Controller with Secured {

  override val userService: UserService = userSer

  def index = withAuth { username => implicit request =>
    Ok(views.html.admin.adminarea(addTripForm)(username, adminService.getAllTrips))
  }


  val addTripForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "description" -> text,
      "shortName" -> nonEmptyText,
      "startDate" -> jodaLocalDate,
      "endDate" -> jodaLocalDate
    )(Trip.apply)(Trip.unapply)
  )


  def addTrip = withAuth { username => implicit request =>
    addTripForm.bindFromRequest.fold(
      formWithErrors => BadRequest("Nope"),
      trip => {
        adminService.insertTrip(trip);
        Redirect(routes.AdminController.index)
      }
    )
  }

  def tripDetails(id: String) = withAuth { username => implicit request =>
    val trip = adminService.getTripById(id)

    trip match {
      case Some(x) => Ok(views.html.admin.admintripdetails(addDayTourForm)(username, x, adminService.getAllDayTours(x.shortName)))
      case None => BadRequest
    }

  }

  val addDayTourForm = Form(
    tuple(
      "date" -> jodaLocalDate,
      "startPoint" -> number,
      "endPoint" -> number,
      "description" -> text,
      "weatherCond" -> text,
      "roadCond" -> text
    )
  )

  def addDayTour(tripId: String) = withAuth { username => implicit request =>
    addDayTourForm.bindFromRequest.fold(
    formWithErrors => BadRequest("Nope"), {
      case (date: LocalDate, startPoint: Int, endPoint: Int, description: String, weatherCond: String, roadCond: String) => {
        val id = UUID.randomUUID()
        adminService.insertDayTour(DayTour(date, id, tripId, startPoint, endPoint), DayTourMetaData(id, description, weatherCond, roadCond))
        Redirect(routes.AdminController.tripDetails(tripId))
      }
    }
    )
  }

}
