package at.droelf.backend.storage.database

import java.util.UUID

import at.droelf.backend.DateTimeUtil
import models._
import org.joda.time._
import parser.gpxtype.GPXTrack

import scala.slick.driver.JdbcProfile

class DBStorageService(val profile: JdbcProfile = SlickDBDriver.getDriver) extends DateTimeUtil {

  val db = new DBConnection(profile).dbObject()

  def saveTracks(tracks: List[GPXTrack], dateTimeZone: DateTimeZone, activity: String) = {

    def isOverViewPt(index: Int, gap: Int, numOfPt: Int): Boolean = {
      (index % gap == 0 || index == 0 || index == numOfPt - 1)
    }

    def getLocalDates(track: GPXTrack, timeZone: DateTimeZone): Seq[LocalDate] = {
      track.trackSegments.map(trkSeg => trkSeg.trackPoints.map(
        wpt => DateTime.parse(wpt.time.get).withZone(timeZone).toLocalDate
      )).flatten.distinct

    }

    db.withTransaction { implicit session =>

      tracks.foreach { track =>
        val id = getRandomId
        Tracks.insertTrack(Track(id, track.name, activity))
        getLocalDates(track, dateTimeZone).foreach(date => TrackToLocalDates.insertTrackToLocalDate(TrackToLocalDate(id, date)))

        TrackMetaDatas.insertTrackMetaData(gpxTrackToTrackMetaData(id, track))

        track.trackSegments.foreach { trkSeg =>
          trkSeg.trackPoints.zipWithIndex.foreach { e =>
            val wpt = e._1
            val time = dateTimeToUtcLocalDateTime(DateTime.parse(wpt.time.get))
            TrackPoints.insertIfNotExists(TrackPoint(id, wpt.latitude, wpt.longitude, wpt.ele.getOrElse(0), time, dateTimeZone, isOverViewPt(e._2, 100, trkSeg.trackPoints.length)))
          }
        }
      }
    }
  }

  def getAllTracks() = db.withTransaction { implicit session => Tracks.getAllTracks}

  def getTrackById(trackId: UUID): Option[Track] = db.withTransaction { implicit session => Tracks.getTrackById(trackId)}

  def getTrackByDate(date: LocalDate): Seq[Track] = db.withTransaction { implicit session =>
    TrackToLocalDates.getTrackToLocalDateByDate(date).map(e => Tracks.getTracksById(e.trackId)).flatten
  }

  def getAllTrackPointsForTrackId(trackId: UUID) = db.withTransaction { implicit session => TrackPoints.getTrackPointsForTrack(trackId)}

  def getTrackPointsForOverview(trackId: UUID): Seq[TrackPoint] = db.withTransaction { implicit session => TrackPoints.getTrackPointsForOverView(trackId)}

  def getNoOfTrackPoints(trackId: UUID) = db.withTransaction { implicit session => TrackPoints.getNoOfTrackPoints(trackId)}

  def getMetaDataForTrackId(trackId: UUID) = db.withTransaction { implicit session => TrackMetaDatas.getTrackMetaDataForTrackId(trackId)}

  def updateTrack(track: Track) = db.withTransaction { implicit session => Tracks.update(track)}

  def updateTrackMetadata(trackMetaData: TrackMetaData) = db.withTransaction { implicit sessoin => TrackMetaDatas.update(trackMetaData)}


  def deleteTrack(trackId: UUID) = db.withTransaction { implicit session =>
    TrackPoints.deleteTrackPoints(trackId)
    TrackMetaDatas.deleteTrackMetaData(trackId)
    TrackToLocalDates.delete(trackId)
    Tracks.deleteTrack(trackId)
  }

  def getDatesForTrack(trackId: UUID) = db.withTransaction { implicit session => TrackToLocalDates.getTrackToLocalDateByTrack(trackId).map(_.date)}


  def getAllTrips(): Seq[Trip] = db.withTransaction { implicit session => Trips.getAllTrips()}

  def getTripById(tripId: String): Option[Trip] = db.withTransaction { implicit session => Trips.getTripById(tripId)}

  def insertTrip(trip: Trip) = db.withTransaction { implicit session => Trips.insertTrip(trip)}

  def updateTrip(trip: Trip) = db.withTransaction { implicit session => Trips.updateTrip(trip)}

  def deleteTrip(tripId: String) = db.withTransaction { implicit session => Trips.deleteTrip(tripId)}


  def getAllDayTours(): Seq[DayTour] = db.withTransaction { implicit session => DayTours.getAllDayTours}

  def getDayTourByLocalDate(startDate: LocalDate, endDate: LocalDate): Seq[DayTour] = db.withTransaction { implicit session => DayTours.getDayToursByDate(startDate, endDate)}

  def getDayTour(dayTourId: UUID): Option[DayTour] = db.withTransaction { implicit session => DayTours.getDayTourById(dayTourId)}

  def insertDayTour(dayTour: DayTour) = db.withTransaction { implicit session => DayTours.insertDayTour(dayTour)}

  def updateDayTour(dayTour: DayTour) = db.withTransaction { implicit session => DayTours.updateDayTour(dayTour)}

  def deleteDayTour(dayTourId: UUID) = db.withTransaction { implicit session => DayTours.deleteDayTour(dayTourId)}


  def insertTrackWithEmptyMetaData(track: Track) = db.withTransaction { implicit session =>
    Tracks.insertTrack(track)
    val trackMetaData = TrackMetaData(track.trackId, None, None, None, None, None, None, None, None, None, None, None, None, None, None, None, None, None, None)
    TrackMetaDatas.insertTrackMetaData(trackMetaData)
  }

  def insertTrackMetadata(trackMetaData: TrackMetaData) = db.withTransaction { implicit session => TrackMetaDatas.insertTrackMetaData(trackMetaData)}

  def insertTrackPoint(trackPoint: TrackPoint) = db.withTransaction { implicit session => TrackPoints.insertIfNotExists(trackPoint)}

  def insertTrackToLocalDate(trackId: UUID, localDate: LocalDate) = db.withTransaction { implicit session => TrackToLocalDates.insertTrackToLocalDate(TrackToLocalDate(trackId, localDate))}


  def insertImage(image: Image) = db.withTransaction { implicit session => Images.insertImage(image)}

  def getImagesForLocalDate(date: LocalDate) = db.withTransaction { implicit session => Images.getImagesByLocalDate(date)}

  def getNewestImagesForTimeRange(startDate: LocalDate, endDate: LocalDate, numberOfImg: Int) = db.withTransaction { implicit session =>
    Images.getNewestImagesForTimeRange(startDate, endDate, numberOfImg)
  }

  def getAllImages = db.withTransaction { implicit session => Images.getAllImages}

  def getImageById(uuid: UUID) = db.withTransaction { implicit session => Images.getImageById(uuid)}

  def deleteImage(uuid: UUID) = db.withTransaction { implicit session => Images.delete(uuid)}

  def updateImage(image: Image) = db.withTransaction { implicit session => Images.update(image)}


  def insertDemoTrips() {
    db.withTransaction { implicit session =>
      Trips.insertTrip(Trip("Pacific Coast Tour", "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.", "pacific", new LocalDate(2014, 5, 20), new LocalDate(2014, 6, 30)))

      val st = new LocalDate(2014, 5, 20)
      val en = new LocalDate(2014, 6, 4)

      for (i <- (0 to Days.daysBetween(st, en).getDays)) {
        val id = getRandomId
        DayTours.insertDayTour(DayTour(st.plusDays(i), id, "title", "<p><b>test</b></p>asdf bl asbasd <u>asdfasdf</u>", "Sunny, rainy", "Asphalt", s"Category: ${Math.round(i / 10)}"))
      }

    }
  }

  // TODO auslagern!
  def getRandomId = UUID.randomUUID()

  private def gpxTrackToTrackMetaData(id: UUID, track: GPXTrack): TrackMetaData = (track.trackStatsExtension, track.trackExtension) match {
    case (Some(trackStats), Some(trackExt)) => TrackMetaData(id, track.desc, trackStats.distance, trackStats.timerTime, trackStats.totalElapsedTime, trackStats.movingTime, trackStats.stoppedTime, trackStats.movingSpeed, trackStats.maxSpeed, trackStats.maxElevation, trackStats.minElevation, trackStats.ascent, trackStats.descent, trackStats.avgAscentRate, trackStats.maxAscentRate, trackStats.avgDescentRate, trackStats.maxDescentRate, trackStats.calories, trackStats.avgHeartRate)
    case (Some(trackStats), None) => TrackMetaData(id, track.desc, trackStats.distance, trackStats.timerTime, trackStats.totalElapsedTime, trackStats.movingTime, trackStats.stoppedTime, trackStats.movingSpeed, trackStats.maxSpeed, trackStats.maxElevation, trackStats.minElevation, trackStats.ascent, trackStats.descent, trackStats.avgAscentRate, trackStats.maxAscentRate, trackStats.avgDescentRate, trackStats.maxDescentRate, trackStats.calories, trackStats.avgHeartRate)
    case (None, Some(trackExt)) => TrackMetaData(id, track.desc, None, None, None, None, None, None, None, None, None, None, None, None, None, None, None, None, None)
    case (None, None) => TrackMetaData(id, track.desc, None, None, None, None, None, None, None, None, None, None, None, None, None, None, None, None, None)
  }

}
