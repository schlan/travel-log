package controllers

import java.util.UUID

import at.droelf.backend.{ControllerUtils, Secured}
import at.droelf.backend.service.{AdminService, UserService}
import at.droelf.gui.entities.{AdminTrackMetaData, AdminTrack, AdminDayTour}
import models.{Track, DayTour, Trip}
import org.joda.time.LocalDate
import play.api.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.data.validation.Constraint
import play.api.mvc.{SimpleResult, Controller}

import scala.util.{Success, Failure, Try}

class AdminController(userSer: UserService, adminService: AdminService) extends Controller with Secured with ControllerUtils with FormTypes{

  override val userService: UserService = userSer

  def index = withAuth { username => implicit request =>
    Ok(views.html.admin.adminarea(addTripForm, dayTourForm)(username, adminService.getAllTrips, adminService.getAllTracks, adminService.getAllDayTours))
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
      case Some(x) => {
        Ok(views.html.admin.details.admintripdetails(addTripForm.fill(x))(username, x))
      }
      case None => BadRequest
    }
  }

  def updateTrip = withAuth { username => implicit request =>
    addTripForm.bindFromRequest.fold(
      formWithErrors => BadRequest("Nope"),{
        trip => {
          adminService.updateTrip(trip)
          Redirect(routes.AdminController.tripDetails(trip.shortName))
        }
     })
  }

  def deleteTrip(tripId: String) = withAuth { username => implicit request =>
    adminService.deleteTrip(tripId)
    Redirect(routes.AdminController.index)
  }

  /* DayTour */

  import play.api.data.format.Formats._
  val dayTourForm = Form(
    mapping(
      "date" -> jodaLocalDate,
      "startPointLat" -> Forms.of[Float],
      "startPointLon" -> Forms.of[Float],
      "endPointLat" -> Forms.of[Float],
      "endPointLon" -> Forms.of[Float],
      "description" -> text,
      "weatherCond" -> text,
      "roadCond" -> text
    )(AdminDayTour.apply)(AdminDayTour.unapply)
  )

  def addDayTour = withAuth { username => implicit request =>
    dayTourForm.bindFromRequest.fold(
    formWithErrors => BadRequest("Nope"), {
      adminDayTour => {
        adminService.insertDayTour(adminDayTour)
        Redirect(routes.AdminController.index)
      }
    })
  }

  def dayTourDetails(dayTourId: String) = withAuth { username => implicit request =>
    parseUUID(dayTourId, uuid => {
      adminService.getDayTourById(uuid) match{
        case Some(dayTour) => Ok(views.html.admin.details.admindaytourdetails(dayTourForm.fill(AdminDayTour(dayTour)))(dayTour))
        case None => NotFound
      }
    })
  }

  def deleteDayTour(dayTourId: String) = withAuth { username => implicit request =>
    parseUUID(dayTourId, uuid => {
      adminService.deleteDayTour(uuid)
      Redirect(routes.AdminController.index)
    })
  }

  def updateDayTour(dayTourId: String) = withAuth { username => implicit request =>
    def parseForm(uuid: UUID) = dayTourForm.bindFromRequest.fold(
      formWithErrors => BadRequest("Nope"),
      dayTour => {
        adminService.updateDayTour(dayTour, uuid)
        Redirect(routes.AdminController.dayTourDetails(uuid.toString))
      }
    )
    parseUUID(dayTourId, parseForm(_))
  }

  /* tracks */

  val trackForm = Form(
    mapping(
      "name" -> optional(text),
      "activity" -> text
    )(AdminTrack.apply)(AdminTrack.unapply)
  )

  val trackMetadataForm = Form(
    mapping(
      "description" -> optionalText,
      "distance" -> optionalFloat,
      "timerTime" -> optionalFloat,
      "totalElapsedTime" -> optionalFloat,
      "movingTime" -> optionalFloat,
      "stoppedTime" -> optionalFloat,
      "movingSpeed" -> optionalFloat,
      "maxSpeed" -> optionalFloat,
      "maxElevation" -> optionalFloat,
      "minElevation" -> optionalFloat,
      "ascent" -> optionalFloat,
      "descent" -> optionalFloat,
      "avgAscentRate" -> optionalFloat,
      "maxAscentRate" -> optionalFloat,
      "avgDescentRate" -> optionalFloat,
      "maxDescentRate" -> optionalFloat,
      "calories" -> optionalFloat,
      "avgHeartRate" -> optionalFloat
    )(AdminTrackMetaData.apply)(AdminTrackMetaData.unapply)
  )

  def trackDetails(id: String) = withAuth{ username => implicit request =>
    parseUUID(id,
      uuid => {
        adminService.getTrackById(uuid) match {
          case Some(track) => Ok(views.html.admin.details.admintrackdetails(trackForm.fill(AdminTrack(track)), trackMetadataForm.fill(AdminTrackMetaData(adminService.getTrackMetadata(track.trackId))))(track,adminService.getTrackMetadata(track.trackId),adminService.getNoOfTrackPoints(track.trackId),adminService.getDatesForTrack(track.trackId)))
          case None => NotFound
        }
      })
  }

  def deleteTrack(id: String) = withAuth { username => implicit request =>
    parseUUID(id,
      uuid => {
        adminService.deleteCompleteTrack(uuid)
        Redirect(routes.AdminController.index())
     })
  }

  def insertDemoData = withAuth { username => implicit request =>
    adminService.insertDemoData
    Redirect(routes.AdminController.index())
  }

  def updateTrack(trackId: String) = withAuth { username => implicit request =>
    def parseForm(uuid: UUID) = trackForm.bindFromRequest.fold(
      formWithErrors => BadRequest("Nope"),
      track => {
        adminService.updateTrack(track, uuid)
        Redirect(routes.AdminController.trackDetails(uuid.toString))
      }
    )
   parseUUID(trackId, parseForm(_))
  }

  def updateTrackMetadata(trackId: String) = withAuth { username => implicit request =>
    def parseForm(uuid: UUID) = trackMetadataForm.bindFromRequest.fold(
      formWithErrors => BadRequest("Nope"),
      trackMetadata => {
        adminService.updateTrackMetadata(trackMetadata, uuid)
        Redirect(routes.AdminController.trackDetails(uuid.toString))
      }
    )
    parseUUID(trackId, parseForm(_))
  }

}

trait FormTypes {

  import play.api.data.format.Formats._

  val float = Forms.of[Float]
  val optionalFloat = optional(float)
  val optionalText = optional(text)

}

