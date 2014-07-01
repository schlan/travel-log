package models

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.joda.PlayJodaSupport
import play.api.db.slick.joda.PlayJodaSupport._
import org.joda.time.LocalDate
import java.util.UUID

case class DayTour(date: LocalDate, dayTourId: UUID, tripId: String, startPoint: Long, endPoint: Long)


class DayTourTable(tag: Tag) extends Table[DayTour](tag, "DAY_TOUR"){

  def date = column[LocalDate]("DATE")
  def dayTourId = column[UUID]("DAYTOUR_ID", O.PrimaryKey)
  def tripId = column[String]("TRIP_ID",O.NotNull)

  def startPoint = column[Long]("DAYTOUR_START")
  def endPoint = column[Long]("DAYTOUR_END")

  def * = (date, dayTourId, tripId, startPoint, endPoint) <> (DayTour.tupled, DayTour.unapply)

  def trip = foreignKey("TRIP_FK", tripId, TableQuery[TripTable])(_.shortName)

  def idx = index("INDEX_DATE", date, unique = true)
}

object DayTours{

  var dayTourTable = TableQuery[DayTourTable]

  def insertDayTour(dayTour: DayTour)(implicit session: Session) = {
    dayTourTable.insert(dayTour)
  }

  def getDayToursByTripId(tripId: String)(implicit session: Session): Seq[DayTour] = {
    dayTourTable.filter(_.tripId === tripId).list
  }

  def getDayToursd(tripId: String, dayTourId: UUID)(implicit session: Session): Option[DayTour] = {
    val dayTours = dayTourTable.filter(_.tripId === tripId).filter(_.dayTourId === dayTourId).list
    if(dayTours.isEmpty) None
    else Some(dayTours.head)
  }
}