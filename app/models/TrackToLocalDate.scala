package models


import org.joda.time.{LocalDate}
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.joda.PlayJodaSupport._
import java.util.UUID


case class TrackToLocalDate(trackId: UUID, date: LocalDate)

class TrackToLocalDateTable(tag: Tag) extends Table[TrackToLocalDate](tag, "TRACK_TO_LOCALDATE"){

  def trackId = column[UUID]("TRACK_ID",O.NotNull)

  def localDate = column[LocalDate]("LOCAL_DATE", O.NotNull)

  override def * = (trackId, localDate) <>(TrackToLocalDate.tupled, TrackToLocalDate.unapply)

  def track = foreignKey("TRACK_TO_LOCAL_DATE_FK", trackId, TableQuery[TrackTable])(_.trackId)

}


object TrackToLocalDates{
  val trackToLocalDateTable = TableQuery[TrackToLocalDateTable]

  def insertTrackToLocalDate(trackToLocalDate: TrackToLocalDate)(implicit session: Session) = {
    if(!trackToLocalDateTable.filter(e => (e.trackId === trackToLocalDate.trackId && e.localDate === trackToLocalDate.date)).exists.run) {
      trackToLocalDateTable.insert(trackToLocalDate)
    }
  }

  def delete(trackId: UUID)(implicit session: Session) = {
    trackToLocalDateTable.filter(_.trackId === trackId).delete
  }

  def getTrackToLocalDateByDate(localDate: LocalDate)(implicit session: Session): Seq[TrackToLocalDate] = {
    trackToLocalDateTable.filter(_.localDate === localDate).list
  }

  def getTrackToLocalDateByTrack(trackId: UUID)(implicit session: Session): Seq[TrackToLocalDate] = {
    trackToLocalDateTable.filter(_.trackId === trackId).list
  }

}