package at.droelf.backend.service

import java.util.UUID

import at.droelf.backend.storage.database.DBStorageService
import at.droelf.gui.entities.{GuiTrackMetaData, AdminTrackMetaData, AdminTrack, AdminDayTour}
import models.{TrackMetaData, Track, DayTour, Trip}
import org.joda.time.{Interval, Period, LocalDate}


class AdminService(dbStorage: DBStorageService) {


  /* Trips */
  def getAllTrips = dbStorage.getAllTrips()
  def updateTrip(trip: Trip) = dbStorage.updateTrip(trip)
  def insertTrip(trip: Trip) = dbStorage.insertTrip(trip)
  def deleteTrip(tripId: String) = dbStorage.deleteTrip(tripId)
  def getTripById(id: String) = dbStorage.getTripById(id)


  /* DayTour */
  def getAllDayTours: Map[String, Seq[DayTour]] = {
    def mapDayToursToTrip(dayTours: Seq[DayTour],startDate: LocalDate, endDate: LocalDate):Seq[DayTour] =  {
      (for{
        dayTour <- dayTours
        if( (dayTour.date.isAfter(startDate) || dayTour.date.isEqual(startDate)) && (dayTour.date.isBefore(endDate) || dayTour.date.isEqual(endDate)) )
      } yield (dayTour)).sortWith((d1,d2) =>d1.date.isBefore(d2.date))
    }

    val dayTours = dbStorage.getAllDayTours
    val trips = getAllTrips

    val unMappedTours = Map[String, Seq[DayTour]]("Unkown" -> dayTours.diff(trips.map(trip => mapDayToursToTrip(dayTours,trip.startDate,trip.endDate)).flatten))

    (trips.map{ trip =>
      (trip.title -> mapDayToursToTrip(dayTours, trip.startDate, trip.endDate))
    }.toMap) ++ unMappedTours
  }

  def getAllDayTours(startDate: LocalDate, endDate: LocalDate) = dbStorage.getDayTourByLocalDate(startDate, endDate)

  def insertDayTour(dayTour: AdminDayTour) = dbStorage.insertDayTour(convertAdminDayTour(dayTour,dbStorage.getRandomId))

  def getDayTourById(dayTourId: UUID): Option[DayTour] = dbStorage.getDayTour(dayTourId)

  def updateDayTour(tour: AdminDayTour, uuid: UUID) = dbStorage.updateDayTour(convertAdminDayTour(tour, uuid))

  def deleteDayTour(dayTour: UUID) = dbStorage.deleteDayTour(dayTour)

  def convertAdminDayTour(dayTour: AdminDayTour, uuid: UUID):DayTour = DayTour(dayTour.date,uuid,dayTour.startPointLat,dayTour.startPointLon,dayTour.endPointLat,dayTour.endPointLon,dayTour.description,dayTour.weatherCond,dayTour.roadCond)



  /* Tracks */
  def updateTrack(track: AdminTrack, trackId: UUID) = dbStorage.updateTrack(convertAdminTrack(track, trackId))

  def getAllTracks = dbStorage.getAllTracks()

  def deleteCompleteTrack(trackId: UUID) = dbStorage.deleteTrack(trackId)

  def getTrackById(trackId: UUID) = dbStorage.getTrackById(trackId)

  def getTrackMetadata(trackId: UUID) = dbStorage.getMetaDataForTrackId(trackId)

  def getTrackPoints(trackId: UUID) = dbStorage.getAllTrackPointsForTrackId(trackId)

  def getNoOfTrackPoints(trackId: UUID) = dbStorage.getNoOfTrackPoints(trackId)

  def getDatesForTrack(trackId: UUID) = dbStorage.getDatesForTrack(trackId)

  def updateTrackMetadata(trackMetadata: AdminTrackMetaData, trackId: UUID) = dbStorage.updateTrackMetadata(convertAdminTrackMetadata(trackMetadata, trackId))

  def convertAdminTrack(track: AdminTrack, trackId: UUID): Track = Track(trackId, track.name, track.activity)

  def convertAdminTrackMetadata(metaData: AdminTrackMetaData, trackId: UUID): TrackMetaData = TrackMetaData(trackId, metaData.description, metaData.distance, metaData.timerTime, metaData.totalElapsedTime, metaData.movingTime, metaData.stoppedTime, metaData.movingSpeed, metaData.maxSpeed, metaData.maxElevation, metaData.minElevation, metaData.ascent, metaData.descent, metaData.avgAscentRate, metaData.maxAscentRate, metaData.avgDescentRate, metaData.maxDescentRate, metaData.calories, metaData.avgHeartRate)



  def insertDemoData = dbStorage.insertDemoTrips
}
