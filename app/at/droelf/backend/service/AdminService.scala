package at.droelf.backend.service

import java.util.UUID

import at.droelf.backend.storage.database.DBStorageService
import models.{DayTour, Trip}
import org.joda.time.{Interval, Period, LocalDate}


class AdminService(dbStorage: DBStorageService) {


  def getAllTrips = dbStorage.getAllTrips()

  def getAllTracks = dbStorage.getAllTracks()

  def getAllDayTours: Map[String, Seq[DayTour]] = {

    def mapDayToursToTrip(dayTours: Seq[DayTour],startDate: LocalDate, endDate: LocalDate):Seq[DayTour] =  {
      for{
        dayTour <- dayTours
        if( (dayTour.date.isAfter(startDate) || dayTour.date.isEqual(startDate)) && (dayTour.date.isBefore(endDate) || dayTour.date.isEqual(endDate)) )
      } yield (dayTour)
    }

    val dayTours = dbStorage.getAllDayTours
    val trips = getAllTrips

    val unMappedTours = Map[String, Seq[DayTour]]("Unkown" -> dayTours.diff(trips.map(trip => mapDayToursToTrip(dayTours,trip.startDate,trip.endDate)).flatten))

    (trips.map{ trip =>
      (trip.title -> mapDayToursToTrip(dayTours, trip.startDate, trip.endDate))
    }.toMap) ++ unMappedTours
  }



  def insertTrip(trip: Trip) = dbStorage.insertTrip(trip)

  def getTripById(id: String) = dbStorage.getTripById(id)

  def getAllDayTours(startDate: LocalDate, endDate: LocalDate) = dbStorage.getDayTourByLocalDate(startDate, endDate)

  def insertDayTour(dayTour: DayTour) = dbStorage.insertDayTour(dayTour)



  def getTrackById(trackId: UUID) = dbStorage.getTrackById(trackId)

  def getTrackMetadata(trackId: UUID) = dbStorage.getMetaDataForTrackId(trackId)

  def getTrackPoints(trackId: UUID) = dbStorage.getAllTrackPointsForTrackId(trackId)

  def deleteCompleteTrack(trackId: UUID) = dbStorage.deleteTrack(trackId)
}
