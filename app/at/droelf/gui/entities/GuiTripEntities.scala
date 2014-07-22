package at.droelf.gui.entities

import org.joda.time.LocalDate
import models.{DayTour, Trip}
import java.util.UUID

case class GuiTrip(title: String, description: String, shortName: String, startDate: LocalDate, endDate: LocalDate, guiDayTours: Seq[GuiDayTour])

object GuiTrip{
  def apply(trip: Trip, guiDayTours: Seq[GuiDayTour]): GuiTrip = GuiTrip(trip.title, trip.description, trip.shortName, trip.startDate, trip.endDate, guiDayTours)
}

case class GuiDayTour(date: LocalDate, dayTourId: UUID, startPoint: Long, endPoint: Long, description: String, weatherCond: String, roadCond: String)

object GuiDayTour{
  def apply(dayTour: DayTour): GuiDayTour = GuiDayTour(dayTour.date,dayTour.dayTourId,dayTour.startPoint,dayTour.endPoint, dayTour.description, dayTour.weatherCond, dayTour.roadCond)
}
