package models

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.joda.PlayJodaSupport
import play.api.db.slick.joda.PlayJodaSupport._
import org.joda.time.LocalDate
import java.util.UUID

case class DayTour(date: LocalDate, dayTourId: UUID, description: String, weatherCond: String, roadCond: String, category: String)

class DayTourTable(tag: Tag) extends Table[DayTour](tag, "DAY_TOUR"){

  def date = column[LocalDate]("DATE")
  def dayTourId = column[UUID]("DAYTOUR_ID", O.PrimaryKey)

  def description = column[String]("DESCRIPTION")
  def weatherCond = column[String]("WEATHER_COND")
  def roadCond = column[String]("ROAD_COND")

  def category = column[String]("CATEGORY")

  def * = (date, dayTourId, description, weatherCond, roadCond, category) <> (DayTour.tupled, DayTour.unapply)

  def idx = index("INDEX_DATE", date, unique = true)
}

object DayTours{

  var dayTourTable = TableQuery[DayTourTable]

  def insertDayTour(dayTour: DayTour)(implicit session: Session) = {
    dayTourTable.insert(dayTour)
  }

  def getDayToursByDate(startDate: LocalDate, endDate: LocalDate)(implicit session: Session): Seq[DayTour] = {
    dayTourTable.filter(_.date >= startDate).filter(_.date <= endDate).list
  }

  def getDayTourById(dayTourId: UUID)(implicit session: Session): Option[DayTour] = {
    val dayTours = dayTourTable.filter(_.dayTourId === dayTourId).list
    if(dayTours.isEmpty) None
    else Some(dayTours.head)
  }

  def getAllDayTours(implicit session: Session): Seq[DayTour] = dayTourTable.list

  def updateDayTour(dayTour: DayTour)(implicit session: Session) = {
    dayTourTable.filter(_.dayTourId === dayTour.dayTourId).update(dayTour)
  }

  def deleteDayTour(dayTourId: UUID)(implicit session: Session) = {
    dayTourTable.filter(_.dayTourId === dayTourId).delete
  }
}
