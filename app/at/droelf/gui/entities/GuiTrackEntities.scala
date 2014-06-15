package at.droelf.gui.entities

import java.util.UUID
import org.joda.time.DateTime
import models.{Track, TrackMetaData, TrackPoint}


  case class GuiTrack(trackId: UUID, name: Option[String], metaData: GuiTrackMetaData, trackPoints: Seq[GuiTrackPoint])


//  implicit val guitrackReads = (
//    (__ \ 'trackId).read[UUID] and
//    (__ \ 'name).read[Option[String]]
//    )



  object GuiTrack {
    def apply(track: Track, metaData: TrackMetaData, trkPt: Seq[TrackPoint]): GuiTrack = {
      GuiTrack(
        track.trackId,
        track.name,
        GuiTrackMetaData(metaData),
        trkPt.map(GuiTrackPoint(_))
      )
    }

  }

  case class GuiTrackMetaData(description: Option[String],
                              distance: Option[Float],
                              timerTime: Option[Float],
                              totalElapsedTime: Option[Float],
                              movingTime: Option[Float],
                              stoppedTime: Option[Float],
                              movingSpeed: Option[Float],
                              maxSpeed: Option[Float],
                              maxElevation: Option[Float],
                              minElevation: Option[Float],
                              ascent: Option[Float],
                              descent: Option[Float],
                              avgAscentRate: Option[Float],
                              maxAscentRate: Option[Float],
                              avgDescentRate: Option[Float],
                              maxDescentRate: Option[Float],
                              calories: Option[Float],
                              avgHeartRate: Option[Float],
                              avgCadence: Option[Float],
                              displayColor: Option[Int])

  object GuiTrackMetaData {
    def apply(metaData: TrackMetaData): GuiTrackMetaData = GuiTrackMetaData(metaData.description, metaData.distance, metaData.timerTime, metaData.totalElapsedTime, metaData.movingTime, metaData.stoppedTime, metaData.movingSpeed, metaData.maxSpeed, metaData.maxElevation, metaData.minElevation, metaData.ascent, metaData.descent, metaData.avgAscentRate, metaData.maxAscentRate, metaData.avgDescentRate, metaData.maxDescentRate, metaData.calories, metaData.avgHeartRate, metaData.avgCadence, metaData.displayColor)
  }

  case class GuiTrackPoint(latitude: Float, longitude: Float, elevation: Float, dateTime: DateTime)

  object GuiTrackPoint {
    def apply(trkPt: TrackPoint): GuiTrackPoint = GuiTrackPoint(trkPt.latitude, trkPt.longitude, trkPt.elevation, trkPt.dateTime)
  }
