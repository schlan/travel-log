package at.droelf.backend.storage

import parser.gpxtype.GPXTrack
import models._
import org.joda.time.{LocalDate, DateTime}
import play.api.db.slick.DB
import play.api.Play.current
import models.Track
import models.TrackPoint
import java.util.UUID
import java.awt.Color
import at.droelf.gui.entities.GuiTrip

class DBStorageService {

  def saveTracks(tracks: List[GPXTrack], activity: String) = {

    DB.withTransaction{ implicit session =>
      tracks.foreach { track =>
        val id = getRandomId
        Tracks.insertTrack(Track(id, track.name, activity))
        TrackMetaDatas.insertTrackMetaData(gpxTrackToTrackMetaData(id, track))

        track.trackSegments.foreach{ trkSeg =>
          trkSeg.trackPoints.foreach{ wpt =>
            TrackPoints.insertIfNotExists(TrackPoint(id, wpt.latitude, wpt.longitude, wpt.ele.getOrElse(0), DateTime.parse(wpt.time.get)))
          }
        }
      }
    }
  }

  def getAllTracks(): Seq[Track] = DB.withSession{implicit session => Tracks.getAllTracks()}

  def getTrackById(trackId: UUID): Option[Track] = DB.withSession{implicit session => Tracks.getTrackById(trackId)}

  def getTrackByDate(date: LocalDate): Seq[Track] = DB.withSession{implicit session => Tracks.getTracksByDate(date)}

  def getAllTrackPointsForTrackId(trackId: UUID) =  DB.withSession{implicit session => TrackPoints.getTrackPointsForTrack(trackId)}

  def getMetaDataForTrackId(trackId: UUID) = DB.withSession{implicit  session => TrackMetaDatas.getTrackMetaDataForTrackId(trackId)}



  def getAllTrips(): Seq[Trip] = DB.withSession{implicit session => Trips.getAllTrips()}

  def getTripById(tripId: String): Option[Trip] = DB.withSession{implicit session => Trips.getTripById(tripId)}

  def insertTrip(trip: Trip) = DB.withSession{implicit session => Trips.insertTrip(trip)}


  def getDayTourByTripId(tripId: String): Seq[DayTour] =  DB.withSession{implicit session => DayTours.getDayToursByTripId(tripId)}

  def getDayTour(tripId: String, dayTourId: UUID): Option[DayTour] = DB.withSession{implicit session => DayTours.getDayToursd(tripId, dayTourId)}

  def getDayTourMetaDataForId(dayTourId: UUID): DayTourMetaData = DB.withSession{implicit  session => DayTourMetaDatas.getDayTourMetaDataById(dayTourId)}

  def insertDayTourWithMetadata(dayTour: DayTour, dayTourMetaData: DayTourMetaData) = DB.withTransaction{implicit session =>
    DayTours.insertDayTour(dayTour)
    DayTourMetaDatas.insert(dayTourMetaData)
  }




  def insertDemoTrips() {
      DB.withTransaction{ implicit session =>
        Trips.insertTrip(Trip("Pacific Coast Tour", "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.", "pacific", new LocalDate(2014,5,20),new LocalDate(2014,5,30)))

        val date = new LocalDate(2014,5,20)

        for(i <- 0 to 50){
          val id = UUID.randomUUID()
          DayTours.insertDayTour(DayTour(date.plusDays(i),id,"pacific",231321,123123))
          DayTourMetaDatas.insert(DayTourMetaData(id,"<p><b>test</b></p>asdf bl asbasd <u>asdfasdf</u>","Sunny, rainy", "Asphalt"))
        }

      }
  }

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
