package models

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.joda.PlayJodaSupport
import play.api.db.slick.joda.PlayJodaSupport._
import org.joda.time.LocalDate


case class Trip(
                 title: String,
                 description: String,
                 shortName: String,
                 startDate: LocalDate,
                 endDate: LocalDate
                 )

class TripTable(tag: Tag) extends Table[Trip](tag, "TRIP") {

  def title = column[String]("TITLE", O.NotNull)

  def description = column[String]("DESC", O.Default("No Description"))

  def shortName = column[String]("SHORT_NAME", O.PrimaryKey)

  def startDate = column[LocalDate]("START_DATE", O.NotNull)

  def endDate = column[LocalDate]("END_DATE", O.NotNull)

  def * = (title, description, shortName, startDate, endDate) <>(Trip.tupled, Trip.unapply)
}

object Trips{

  val tripTable = TableQuery[TripTable]

  def insertTrip(trip: Trip)(implicit session: Session) = {
    tripTable.insert(trip)
  }

  def getAllTrips()(implicit session: Session): Seq[Trip] = {
    tripTable.list
  }

  def getTripById(tripId: String)(implicit session: Session): Option[Trip] = {
    val trips = tripTable.filter(_.shortName === tripId).list
    if(trips.isEmpty) None
    else Some(trips.head)
  }


}