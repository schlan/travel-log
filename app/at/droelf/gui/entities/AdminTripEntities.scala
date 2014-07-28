package at.droelf.gui.entities

import models.{TrackMetaData, Track, DayTour}
import org.joda.time.LocalDate

case class AdminDayTour(date: LocalDate, description: String, weatherCond: String, roadCond: String, category: String)

object AdminDayTour{
  def apply(dayTour: DayTour): AdminDayTour = AdminDayTour(
    dayTour.date,
    dayTour.description,
    dayTour.weatherCond,
    dayTour.roadCond,
    dayTour.category)
}

case class AdminTrack(name: Option[String], activity: String)

object AdminTrack{
  def apply(track: Track): AdminTrack = AdminTrack(track.name,track.activity)
}

case class AdminTrackMetaData(
                          description: Option[String],
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
                          avgHeartRate: Option[Float])

object AdminTrackMetaData{
  def apply(metaData: TrackMetaData): AdminTrackMetaData = AdminTrackMetaData(metaData.description, metaData.distance, metaData.timerTime, metaData.totalElapsedTime, metaData.movingTime, metaData.stoppedTime, metaData.movingSpeed, metaData.maxSpeed, metaData.maxElevation, metaData.minElevation, metaData.ascent, metaData.descent, metaData.avgAscentRate, metaData.maxAscentRate, metaData.avgDescentRate, metaData.maxDescentRate, metaData.calories, metaData.avgHeartRate)
}