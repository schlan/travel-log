package models

import org.joda.time.{LocalDate, DateTime}
import scala.slick.driver.H2Driver.simple._
import com.github.tototoshi.slick.H2JodaSupport._
import java.util.UUID

case class TrackPoint(trackId: UUID, latitude: Float, longitude: Float, elevation: Float, dateTime: DateTime)

class TrackPointTable(tag: Tag) extends Table[TrackPoint](tag, "TRACK_POINTS") {

  def trackId = column[UUID]("TRACK_ID", O.NotNull)
  def latitude = column[Float]("LATITUDE", O.NotNull)
  def longitude = column[Float]("LONGITUDE", O.NotNull)
  def elevation = column[Float]("ELEVATION")
  def dateTime = column[DateTime]("DATE", O.PrimaryKey)

  def * = (trackId, latitude, longitude, elevation, dateTime) <>(TrackPoint.tupled, TrackPoint.unapply)

  def track = foreignKey("TRACK_POINT_FK", trackId, TableQuery[TrackTable])(_.trackId)

  def idx = index("TRACK_POINT_IDX", dateTime, unique = true)
}

object TrackPoints {

  val trackPointTable = TableQuery[TrackPointTable]

  def insertTrackPoint(trackPoint: TrackPoint)(implicit session: Session) = trackPointTable.insert(trackPoint)

  def getTrackPointsForTrack(trackId: UUID)(implicit session: Session): Seq[TrackPoint] = return trackPointTable.filter(_.trackId === trackId).list

}
