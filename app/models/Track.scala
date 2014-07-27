package models

import at.droelf.gui.entities.AdminTrack
import org.joda.time.{LocalTime, LocalDate}
import play.api.Logger
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.joda.PlayJodaSupport
import play.api.db.slick.joda.PlayJodaSupport._
import java.util.UUID

import scala.slick.lifted.ProvenShape


case class Track(trackId:UUID, name: Option[String], activity: String)

class TrackTable(tag: Tag) extends Table[Track](tag, "TRACK") {

  def trackId = column[UUID]("TRACK_ID", O.PrimaryKey)

  def name = column[Option[String]]("TRACK_NAME", O.Nullable)

  def activity = column[String]("TRACK_ACTIVITY_TYPE", O.NotNull)

  override def * = (trackId, name, activity) <> (Track.tupled, Track.unapply)

}

object Tracks{

  val trackTable = TableQuery[TrackTable]
  val trackPoints = TableQuery[TrackPointTable]

  def insertTrack(track: Track)(implicit session: Session) = {
      trackTable.insert(track)
  }

  def getTrackById(trackId: UUID)(implicit session: Session): Option[Track] = {
    val tracks = trackTable.filter(_.trackId === trackId).list
    if(tracks.isEmpty) None
    else Some(tracks.head)
    //TODO
  }

  def getAllTracks(implicit session: Session): Seq[Track]= {
    trackTable.list
  }

  def getTracksById(trackId: UUID)(implicit session: Session): Seq[Track] = {
//
//    val start = date.toLocalDateTime(LocalTime.MIDNIGHT)
//    val end = date.plusDays(1).toLocalDateTime(LocalTime.MIDNIGHT)
//
//    val q = (for {
//      trkPt <- trackPoints
//      trk <- trackTable if (trkPt.trackId === trk.trackId) && (trkPt.dateTime >= start) && (trkPt.dateTime < end)
//    }yield(trk)).list
//
//    return q.distinct
    trackTable.filter(_.trackId === trackId).list
  }

  def deleteTrack(trackId: UUID)(implicit session: Session) = {
    trackTable.filter(_.trackId === trackId).delete
  }

  def update(track: Track)(implicit session: Session) = {
    trackTable.filter(_.trackId === track.trackId).update(track)
  }
}
