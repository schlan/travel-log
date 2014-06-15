package models

import org.joda.time.LocalDate
import play.api.db.slick.Config.driver.simple._
import com.github.tototoshi.slick.H2JodaSupport._
import java.util.UUID


case class Track(trackId:UUID, name: Option[String])

class TrackTable(tag: Tag) extends Table[Track](tag, "TRACK") {

  def trackId = column[UUID]("TRACK_ID", O.PrimaryKey)

  def name = column[Option[String]]("TRACK_NAME", O.Nullable)

  override def * = (trackId, name) <> (Track.tupled, Track.unapply)

}


object Tracks{

  val trackTable = TableQuery[TrackTable]
  val trackPoints = TableQuery[TrackPointTable]

  def insertTrack(track: Track)(implicit session: Session) = {
      trackTable.insert(track)
  }

  def getAllTracks()(implicit session: Session): Seq[Track] = {
      return trackTable.list
  }

  def getTrackById(trackId: UUID)(implicit session: Session): Option[Track] = {
    val tracks = trackTable.filter(_.trackId === trackId).list
    if(tracks.isEmpty) None
    else Some(tracks.head)
  }


  def getTracksByDate(date: LocalDate)(implicit session: Session): Seq[Track] = {

    val start = date.toDateTimeAtStartOfDay
    val end = date.plusDays(1).toDateTimeAtStartOfDay

    val q = (for {
      trkPt <- trackPoints
      trk <- trackTable if (trkPt.trackId === trk.trackId) && (trkPt.dateTime >= start) && (trkPt.dateTime < end)
    }yield(trk)).list

    //TODO use groupBy

    return q.distinct
  }

}
