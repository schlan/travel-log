package at.droelf.backend.service

import at.droelf.backend.storage.DBStorageService
import models.{DayTourMetaData, DayTour, Trip}


class AdminService(dbStorage: DBStorageService) {


  def insertTrip(trip: Trip) = dbStorage.insertTrip(trip)

  def getAllTrips() = dbStorage.getAllTrips()

  def getTripById(id: String) = dbStorage.getTripById(id)


  def getAllDayTours(id: String) = dbStorage.getDayTourByTripId(id)

  def insertDayTour(tour: DayTour, data: DayTourMetaData) = {
    dbStorage.insertDayTourWithMetadata(tour, data)
  }


}
