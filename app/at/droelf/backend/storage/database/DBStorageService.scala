package at.droelf.backend.storage.database

import java.awt.Color
import java.util.UUID

import at.droelf.backend.DateTimeUtil
import models._
import org.joda.time._
import parser.gpxtype.GPXTrack

import scala.slick.driver.JdbcProfile

class DBStorageService(val profile: JdbcProfile = SlickDBDriver.getDriver) extends DateTimeUtil{

  val db = new DBConnection(profile).dbObject()

  def saveTracks(tracks: List[GPXTrack], dateTimeZone: DateTimeZone, activity: String) = {
    db.withTransaction{ implicit session =>
      tracks.foreach { track =>
        val id = getRandomId
        Tracks.insertTrack(Track(id, track.name, activity))
        TrackMetaDatas.insertTrackMetaData(gpxTrackToTrackMetaData(id, track))

        track.trackSegments.foreach{ trkSeg =>
          trkSeg.trackPoints.foreach{ wpt =>
            val time = dateTimeToUtcLocalTime(DateTime.parse(wpt.time.get))
            TrackPoints.insertIfNotExists(TrackPoint(id, wpt.latitude, wpt.longitude, wpt.ele.getOrElse(0), time, dateTimeZone))
          }
        }
      }
    }
  }

  def getAllTracks(): Seq[Track] = db.withTransaction{implicit session => Tracks.getAllTracks()}

  def getTrackById(trackId: UUID): Option[Track] = db.withTransaction{implicit session => Tracks.getTrackById(trackId)}

  def getTrackByDate(date: LocalDate): Seq[Track] = db.withTransaction{implicit session => Tracks.getTracksByDate(date)}

  def getAllTrackPointsForTrackId(trackId: UUID) =  db.withTransaction{implicit session => TrackPoints.getTrackPointsForTrack(trackId)}

  def getMetaDataForTrackId(trackId: UUID) = db.withTransaction{implicit  session => TrackMetaDatas.getTrackMetaDataForTrackId(trackId)}

  def deleteTrack(trackId: UUID) = db.withTransaction{implicit  session =>
    TrackPoints.deleteTrackPoints(trackId)
    TrackMetaDatas.deleteTrackMetaData(trackId)
    Tracks.deleteTrack(trackId)
  }


  def getAllTrips(): Seq[Trip] = db.withTransaction{implicit session => Trips.getAllTrips()}

  def getTripById(tripId: String): Option[Trip] = db.withTransaction{implicit session => Trips.getTripById(tripId)}

  def insertTrip(trip: Trip) = db.withTransaction{implicit session => Trips.insertTrip(trip)}


  def getAllDayTours(): Seq[DayTour] = db.withTransaction{implicit session => DayTours.getAllDayTours }

  def getDayTourByLocalDate(startDate: LocalDate, endDate: LocalDate): Seq[DayTour] =  db.withTransaction{implicit session => DayTours.getDayToursByDate(startDate, endDate)}

  def getDayTour(dayTourId: UUID): Option[DayTour] = db.withTransaction{implicit session => DayTours.getDayTourById(dayTourId)}

  def insertDayTour(dayTour: DayTour) = db.withTransaction{implicit session => DayTours.insertDayTour(dayTour)}


  def insertImage(image: Image) = db.withTransaction{implicit session => Images.insertImage(image)}

  def getImagesForLocalDate(date: LocalDate) = db.withTransaction{implicit session => Images.getImagesByLocalDate(date)}



  def insertDemoTrips() {
    db.withTransaction{ implicit session =>
        Trips.insertTrip(Trip("Pacific Coast Tour", "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.", "pacific", new LocalDate(2014,5,20),new LocalDate(2014,6,30)))

        val st = new LocalDate(2014,5,20)
        val en = new LocalDate(2014,6,30)

        for(i <- (0 to Days.daysBetween(st,en).getDays)){
          val id = UUID.randomUUID()
          DayTours.insertDayTour(DayTour(st.plusDays(i),id,231321,123123,"<p><b>test</b></p>asdf bl asbasd <u>asdfasdf</u>","Sunny, rainy", "Asphalt"))
        }

      }
  }

  // TODO auslagern!
  private def getRandomId = UUID.randomUUID()

  private def gpxTrackToTrackMetaData(id: UUID, track: GPXTrack): TrackMetaData = (track.trackStatsExtension, track.trackExtension) match{
      case (Some(trackStats), Some(trackExt)) => TrackMetaData(id,track.desc,trackStats.distance,trackStats.timerTime,trackStats.totalElapsedTime,trackStats.movingTime,trackStats.stoppedTime,trackStats.movingSpeed,trackStats.maxSpeed,trackStats.maxElevation,trackStats.minElevation,trackStats.ascent,trackStats.descent,trackStats.avgAscentRate,trackStats.maxAscentRate,trackStats.avgDescentRate,trackStats.maxDescentRate,trackStats.calories,trackStats.avgHeartRate,trackStats.avgCadence,displayColorConverter(trackExt.displayColor))
      case (Some(trackStats), None) => TrackMetaData(id,track.desc,trackStats.distance,trackStats.timerTime,trackStats.totalElapsedTime,trackStats.movingTime,trackStats.stoppedTime,trackStats.movingSpeed,trackStats.maxSpeed,trackStats.maxElevation,trackStats.minElevation,trackStats.ascent,trackStats.descent,trackStats.avgAscentRate,trackStats.maxAscentRate,trackStats.avgDescentRate,trackStats.maxDescentRate,trackStats.calories,trackStats.avgHeartRate,trackStats.avgCadence,None)
      case (None, Some(trackExt)) => TrackMetaData(id,track.desc,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,displayColorConverter(trackExt.displayColor))
      case (None, None) => TrackMetaData(id,track.desc,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None)
  }

  private def displayColorConverter(displayColor: Option[String]): Option[Int] = {
    if(!displayColor.isEmpty){
      var color = displayColor.get
      color match{
        case "Black" => Some(Color.BLACK.getRGB)
        case "DarkRed" => Some(Color.getColor("#8B0000").getRGB)
        case "DarkGreen" => Some(Color.getColor("#006400").getRGB)
        case "DarkYellow" => Some(Color.getColor("#DAA520").getRGB)
        case "DarkBlue" => Some(Color.getColor("#00008B").getRGB)
        case "DarkMagenta" => Some(Color.getColor("#8B008B").getRGB)
        case "DarkCyan" => Some(Color.getColor("#008B8B").getRGB)
        case "LightGray" => Some(Color.LIGHT_GRAY.getRGB)
        case "DarkGray" => Some(Color.DARK_GRAY.getRGB)
        case "Red" => Some(Color.RED.getRGB)
        case "Green" => Some(Color.GREEN.getRGB)
        case "Yellow" => Some(Color.YELLOW.getRGB)
        case "Blue" => Some(Color.BLUE.getRGB)
        case "Magenta" => Some(Color.MAGENTA.getRGB)
        case "Cyan" => Some(Color.CYAN.getRGB)
        case "White" => Some(Color.WHITE.getRGB)
        case "Transparent" => Some(Color.getColor("#ff000000").getRGB)
        case _ => Some(Color.BLACK.getRGB)
      }
    }else{
      None
    }
  }



}
