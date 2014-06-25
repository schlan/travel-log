package at.droelf.gui.entities

import java.util.UUID
import org.joda.time.DateTime
import models.{Track, TrackMetaData, TrackPoint}


  case class GuiTrack(trackId: UUID, name: Option[String], activity: String, metaData: GuiTrackMetaData, trackPoints: Seq[GuiTrackPoint])

  object GuiTrack {
    def apply(track: Track, metaData: TrackMetaData, trkPt: Seq[TrackPoint]): GuiTrack = {
      GuiTrack(
        track.trackId,
        track.name,
        track.activity,
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
                              displayColor: Option[Int]){

    def +(other: GuiTrackMetaData): GuiTrackMetaData = GuiTrackMetaData(
        for(d1 <- this.description; d2 <- other.description) yield (d1 + ", " + d2),
        addOptionFloat(this.distance, other.distance),
        addOptionFloat(this.timerTime, other.timerTime),
        addOptionFloat(this.totalElapsedTime, other.totalElapsedTime),
        addOptionFloat(this.movingTime, other.movingTime),
        addOptionFloat(this.stoppedTime, other.stoppedTime),
        averageOptionFloat(this.movingSpeed, other.movingSpeed),
        maxOptionFloat(this.maxSpeed, other.maxSpeed),
        maxOptionFloat(this.maxElevation, other.maxElevation),
        minOptionFloat(this.minElevation, other.minElevation),
        addOptionFloat(this.ascent, other.ascent),
        addOptionFloat(this.descent, other.descent),
        averageOptionFloat(this.avgAscentRate, other.avgAscentRate),
        maxOptionFloat(this.maxAscentRate, other.maxAscentRate),
        averageOptionFloat(this.avgDescentRate, other.avgDescentRate),
        maxOptionFloat(this.maxDescentRate, other.maxDescentRate),
        addOptionFloat(this.calories, other.calories),
        averageOptionFloat(this.avgHeartRate, other.avgHeartRate),
        averageOptionFloat(this.avgCadence, other.avgCadence),
        this.displayColor
      )
    def averageOptionFloat(o1: Option[Float], o2: Option[Float]): Option[Float] = for(d1 <- o1; d2 <- o2) yield ((d1 + d2)/2)
    def minOptionFloat(o1: Option[Float], o2: Option[Float]): Option[Float] = for(d1 <- o1; d2 <- o2) yield (Math.min(d1, d2))
    def maxOptionFloat(o1: Option[Float], o2: Option[Float]): Option[Float] = for(d1 <- o1; d2 <- o2) yield (Math.max(d1, d2))
    def addOptionFloat(o1: Option[Float], o2: Option[Float]): Option[Float] = for(d1 <- o1; d2 <- o2) yield (d1 + d2)

  }

  object GuiTrackMetaData {
    def apply(metaData: TrackMetaData): GuiTrackMetaData = GuiTrackMetaData(metaData.description, metaData.distance, metaData.timerTime, metaData.totalElapsedTime, metaData.movingTime, metaData.stoppedTime, metaData.movingSpeed, metaData.maxSpeed, metaData.maxElevation, metaData.minElevation, metaData.ascent, metaData.descent, metaData.avgAscentRate, metaData.maxAscentRate, metaData.avgDescentRate, metaData.maxDescentRate, metaData.calories, metaData.avgHeartRate, metaData.avgCadence, metaData.displayColor)
  }

  case class GuiTrackPoint(latitude: Float, longitude: Float, elevation: Float, dateTime: DateTime)

  object GuiTrackPoint {
    def apply(trkPt: TrackPoint): GuiTrackPoint = GuiTrackPoint(trkPt.latitude, trkPt.longitude, trkPt.elevation, trkPt.dateTime)
  }

