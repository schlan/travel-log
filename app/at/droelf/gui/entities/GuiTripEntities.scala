package at.droelf.gui.entities

import org.joda.time.LocalDate
import models.{DayTourMetaData, DayTour, Trip}
import java.util.UUID

case class GuiTrip(title: String, description: String, shortName: String, startDate: LocalDate, endDate: LocalDate, guiDayTours: Seq[GuiDayTour])

object GuiTrip{
  def apply(trip: Trip, guiDayTours: Seq[GuiDayTour]): GuiTrip = GuiTrip(trip.title, trip.description, trip.shortName, trip.startDate, trip.endDate, guiDayTours)
}

case class GuiDayTour(date: LocalDate, dayTourId: UUID, startPoint: Long, endPoint: Long, metData: GuiDayTourMetaData)

object GuiDayTour{
  def apply(dayTour: DayTour, metData: DayTourMetaData): GuiDayTour = GuiDayTour(dayTour.date,dayTour.dayTourId,dayTour.startPoint,dayTour.endPoint, GuiDayTourMetaData(metData))
}

case class GuiDayTourMetaData(dayTourId: UUID, description: String, weatherCond: String, roadCond: String)

object GuiDayTourMetaData{
  def apply(metaData: DayTourMetaData): GuiDayTourMetaData = GuiDayTourMetaData(metaData.dayTourId,metaData.description,metaData.weatherCond,metaData.roadCond)
}