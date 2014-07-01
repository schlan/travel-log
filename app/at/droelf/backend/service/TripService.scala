package at.droelf.backend.service

import at.droelf.backend.storage.database.DBStorageService
import at.droelf.gui.entities.{GuiDayTourMetaData, GuiDayTour, GuiTrip}
import java.util.UUID

import models.Trip

class TripService(dbStorage: DBStorageService) {


  def getTripById(tripId: String): Option[GuiTrip] = dbStorage.getTripById(tripId).map(GuiTrip(_, getDayToursByTripId(tripId)))

  def getAllTrips(): Seq[GuiTrip] = dbStorage.getAllTrips().map(trip => GuiTrip(trip, getDayToursByTripId(trip.shortName)))

  def getAllTripsForNavBar(): Seq[(String, String)] = dbStorage.getAllTrips().map(trip => (trip.shortName, trip.title))

  def getDayToursByTripId(tripId: String): Seq[GuiDayTour] = dbStorage.getDayTourByTripId(tripId).map(e => GuiDayTour(e, dbStorage.getDayTourMetaDataForId(e.dayTourId)))

  def getDayTour(tripId: String, dayTourId: UUID): Option[GuiDayTour] = dbStorage.getDayTour(tripId, dayTourId).map(e => GuiDayTour(e, dbStorage.getDayTourMetaDataForId(e.dayTourId)))

  def insertDemoContent() = dbStorage.insertDemoTrips()


}
