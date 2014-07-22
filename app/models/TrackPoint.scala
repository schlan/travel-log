package models

import org.joda.time.{DateTimeZone, LocalDateTime, LocalDate, DateTime}
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.joda.PlayJodaSupport
import play.api.db.slick.joda.PlayJodaSupport._
import java.util.UUID

case class TrackPoint(trackId: UUID, latitude: Float, longitude: Float, elevation: Float, dateTime: LocalDateTime, dateTimeZone: DateTimeZone)

class TrackPointTable(tag: Tag) extends Table[TrackPoint](tag, "TRACK_POINTS") {

  def trackId = column[UUID]("TRACK_ID", O.NotNull)
  def latitude = column[Float]("LATITUDE", O.NotNull)
  def longitude = column[Float]("LONGITUDE", O.NotNull)
  def elevation = column[Float]("ELEVATION")
  def dateTime = column[LocalDateTime]("DATE", O.PrimaryKey)
  def dateTimeZone = column[DateTimeZone]("TIME_ZONE", O.NotNull)

  def * = (trackId, latitude, longitude, elevation, dateTime, dateTimeZone) <>(TrackPoint.tupled, TrackPoint.unapply)

  def track = foreignKey("TRACK_POINT_FK", trackId, TableQuery[TrackTable])(_.trackId)

  def idx = index("TRACK_POINT_IDX", dateTime, unique = true)
}

object TrackPoints {

  val trackPointTable = TableQuery[TrackPointTable]

  def insertTrackPoint(trackPoint: TrackPoint)(implicit session: Session) = trackPointTable.insert(trackPoint)

  def insertIfNotExists(trackPoint: TrackPoint)(implicit session: Session) = {
    if(!trackPointTable.filter(_.dateTime === trackPoint.dateTime).exists.run){
      insertTrackPoint(trackPoint)
    }
  }

  def getTrackPointsForTrack(trackId: UUID)(implicit session: Session): Seq[TrackPoint] = return trackPointTable.filter(_.trackId === trackId).list

  def deleteTrackPoints(trackId: UUID)(implicit session: Session) = {
    trackPointTable.filter(_.trackId === trackId).delete
  }

}
