package at.droelf.backend.service

import at.droelf.backend.storage.database.DBStorageService
import at.droelf.gui.entities.{GuiDayTourMetaData, GuiDayTour, GuiTrip}
import java.util.UUID

import models.Trip
import org.joda.time.LocalDate

class TripService(dbStorage: DBStorageService) {


  def getTripById(tripId: String): Option[GuiTrip] = dbStorage.getTripById(tripId).map(trip => GuiTrip(trip, getDayToursByLocalDate(trip.startDate,trip.endDate)))

  def getAllTrips(): Seq[GuiTrip] = dbStorage.getAllTrips().map(trip => GuiTrip(trip, getDayToursByLocalDate(trip.startDate,trip.endDate)))

  def getAllTripsForNavBar(): Seq[(String, String)] = dbStorage.getAllTrips().map(trip => (trip.shortName, trip.title))


  def getDayToursByLocalDate(startDate: LocalDate, endDate: LocalDate): Seq[GuiDayTour] = dbStorage.getDayTourByLocalDate(startDate,endDate).map(e => GuiDayTour(e, dbStorage.getDayTourMetaDataForId(e.dayTourId)))

  def getDayTourById(dayTourId: UUID): Option[GuiDayTour] = dbStorage.getDayTour(dayTourId).map(e => GuiDayTour(e, dbStorage.getDayTourMetaDataForId(e.dayTourId)))

  def insertDemoContent() = dbStorage.insertDemoTrips()

}
