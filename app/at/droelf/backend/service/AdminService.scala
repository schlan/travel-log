package at.droelf.backend.service

import at.droelf.backend.storage.database.DBStorageService
import models.{DayTourMetaData, DayTour, Trip}
import org.joda.time.LocalDate


class AdminService(dbStorage: DBStorageService) {


  def insertTrip(trip: Trip) = dbStorage.insertTrip(trip)

  def getAllTrips() = dbStorage.getAllTrips()

  def getTripById(id: String) = dbStorage.getTripById(id)


  def getAllDayTours(startDate: LocalDate, endDate: LocalDate) = dbStorage.getDayTourByLocalDate(startDate, endDate)

  def insertDayTour(tour: DayTour, data: DayTourMetaData) = {
    dbStorage.insertDayTourWithMetadata(tour, data)
  }


}
