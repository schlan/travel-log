package at.droelf.gui.entities


import java.util.UUID

import models.{Track, DayTour}
import org.joda.time.LocalDate

case class AdminDayTour(date: LocalDate, startPointLat: Float, startPointLon: Float, endPointLat: Float, endPointLon: Float, description: String, weatherCond: String, roadCond: String)

object AdminDayTour{
  def apply(dayTour: DayTour): AdminDayTour = AdminDayTour(
    dayTour.date,
    dayTour.startPointLat,
    dayTour.startPointLon,
    dayTour.endPointLat,
    dayTour.endPointLon,
    dayTour.description,
    dayTour.weatherCond,
    dayTour.roadCond)
}

case class AdminTrack(name: Option[String], activity: String)

object AdminTrack{
  def apply(track: Track): AdminTrack = AdminTrack(track.name,track.activity)
}