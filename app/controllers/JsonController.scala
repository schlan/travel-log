package controllers

import at.droelf.backend.service.{TripService, ImageService, GpxTrackService}
import at.droelf.gui.entities._
import org.joda.time.LocalDate
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.util.{Failure, Success, Try}

class JsonController(gpxTrackService: GpxTrackService, imageService: ImageService, tripService: TripService) extends Controller with JsonSerializer {

  def getDayTourInformation(localDate: String) = Action {

    val parsedDate = Try(LocalDate.parse(localDate))

    parsedDate match {
      case Success(date) => {
        val trackInformation = gpxTrackService.getTrackInformationForLocalDate(date)
        val images = imageService.getImagesForDate(date)

        Ok(Json.toJson(DayTourInformationResponse(trackInformation._1, trackInformation._2, images)))

      }
      case Failure(e) => NotFound
    }
  }

  def getTripInformation(tripId: String) = Action {

    tripService.getTripById(tripId) match {
      case Some(trip) => {

        def getLatestTrackPoint(sortedDate: Seq[LocalDate]): Option[GuiTrackPoint] = {
          sortedDate.nonEmpty match {
            case true => gpxTrackService.getLatestTrackPointForDate(sortedDate.head)
            case false => None
          }
        }

        def getSummarizedMetaData(tracks: Seq[GuiTrack]): Map[String, GuiTrackMetaData] = {

          def sumByType(typ: String, tracks: Seq[GuiTrack]): GuiTrackMetaData = {
            (for {
              track <- tracks
              if (typ == track.activity)
            } yield (track.metaData)).reduce((m1, m2) => m1 + m2)
          }

          val activityTypes = tracks.map(trk => trk.activity).distinct
          val sum = activityTypes.map(t => (t -> sumByType(t, tracks))).toMap
          sum ++ Map("total" -> sum.values.reduce((m1, m2) => m1 + m2))
        }

        val dayTours = trip.guiDayTours
        val sortedDate = dayTours.map(_.date).sortWith((d1, d2) => d1.isAfter(d2))


        val tracks = dayTours.map(dt => gpxTrackService.getCondensedTrackForLocalDate(dt.date)).flatten
        val latestTrkPt = getLatestTrackPoint(sortedDate)
        val metadata = getSummarizedMetaData(tracks)

        Ok(Json.toJson(TripSummaryResponse(latestTrkPt, metadata, tracks, Seq())))
      }
      case None => NotFound
    }
  }


}
