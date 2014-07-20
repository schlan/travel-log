package models

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.joda.PlayJodaSupport
import play.api.db.slick.joda.PlayJodaSupport._
import org.joda.time.{LocalTime, LocalDate}
import java.util.UUID

case class DayTour(date: LocalDate, dayTourId: UUID, startPoint: Long, endPoint: Long)


class DayTourTable(tag: Tag) extends Table[DayTour](tag, "DAY_TOUR"){

  def date = column[LocalDate]("DATE")
  def dayTourId = column[UUID]("DAYTOUR_ID", O.PrimaryKey)

  def startPoint = column[Long]("DAYTOUR_START")
  def endPoint = column[Long]("DAYTOUR_END")

  def * = (date, dayTourId, startPoint, endPoint) <> (DayTour.tupled, DayTour.unapply)

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
}