package models

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.joda.PlayJodaSupport
import play.api.db.slick.joda.PlayJodaSupport._
import org.joda.time.{LocalTime, LocalDate}
import java.util.UUID

case class DayTour(date: LocalDate, dayTourId: UUID, startPointLat: Float, startPointLon: Float, endPointLat: Float, endPointLon: Float, description: String, weatherCond: String, roadCond: String)

class DayTourTable(tag: Tag) extends Table[DayTour](tag, "DAY_TOUR"){

  def date = column[LocalDate]("DATE")
  def dayTourId = column[UUID]("DAYTOUR_ID", O.PrimaryKey)

  def startPointLat = column[Float]("DAYTOUR_START_LAT")
  def startPointLon = column[Float]("DAYTOUR_START_LON")
  def endPointLat = column[Float]("DAYTOUR_END_LAT")
  def endPointLon = column[Float]("DAYTOUR_END_LON")

  def description = column[String]("DESCRIPTION")
  def weatherCond = column[String]("WEATHER_COND")
  def roadCond = column[String]("ROAD_COND")

  def * = (date, dayTourId, startPointLat, startPointLon, endPointLat, endPointLon, description, weatherCond, roadCond) <> (DayTour.tupled, DayTour.unapply)

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

//
//object DayTour{
//  def apply(date: LocalDate, startPoint: Long, endPoint: Long, description: String, weatherCond: String, roadCond: String) {
//    DayTour(date, UUID.randomUUID(), startPoint, endPoint, description, weatherCond, roadCond)
//  }
//  def unapply()
//}