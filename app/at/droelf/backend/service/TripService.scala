package at.droelf.backend.service
import at.droelf.gui.entities.{GuiDayTour, GuiTrip}
import at.droelf.backend.storage.DBStorageService
import java.util.UUID

class TripService(dbStorage: DBStorageService) {

  def getTripById(tripId: String): Option[GuiTrip] = dbStorage.getTripById(tripId).map(GuiTrip(_, getDayToursByTripId(tripId)))

  def getAllTrips(): Seq[GuiTrip] = dbStorage.getAllTrips().map(trip => GuiTrip(trip, getDayToursByTripId(trip.shortName)))

  def getDayToursByTripId(tripId: String): Seq[GuiDayTour] = dbStorage.getDayTourByTripId(tripId).map(GuiDayTour(_))

  def getDayTour(tripId: String, dayTourId: UUID): Option[GuiDayTour] = dbStorage.getDayTour(tripId, dayTourId).map(GuiDayTour(_))

  def insertDemoContent() = dbStorage.insertDemoTrips()

}
