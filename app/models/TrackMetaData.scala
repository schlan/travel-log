package models

import play.api.db.slick.Config.driver.simple._
import java.util.UUID

case class TrackMetaData(
                          trackId: UUID,
                          description: Option[String],
                          distance: Option[Float],
                          timerTime: Option[Float],
                          totalElapsedTime: Option[Float],
                          movingTime: Option[Float],
                          stoppedTime: Option[Float],
                          movingSpeed: Option[Float],
                          maxSpeed: Option[Float],
                          maxElevation: Option[Float],
                          minElevation: Option[Float],
                          ascent: Option[Float],
                          descent: Option[Float],
                          avgAscentRate: Option[Float],
                          maxAscentRate: Option[Float],
                          avgDescentRate: Option[Float],
                          maxDescentRate: Option[Float],
                          calories: Option[Float],
                          avgHeartRate: Option[Float])

class TrackMetaDataTable(tag: Tag) extends Table[TrackMetaData](tag, "TRACK_METADATA") {

  def trackId = column[UUID]("TRACK_ID", O.PrimaryKey)
  def description = column[Option[String]]("DESCRIPTION", O.Nullable, O.DBType("VARCHAR(20000)"))
  def distance = column[Option[Float]]("DISTANCE", O.Nullable)
  def timerTime = column[Option[Float]]("TIMERTIME", O.Nullable)
  def totalElapsedTime = column[Option[Float]]("TOTALELAPSEDTIME", O.Nullable)
  def movingTime = column[Option[Float]]("MOVINGTIME", O.Nullable)
  def stoppedTime = column[Option[Float]]("STOPPEDTIME", O.Nullable)
  def movingSpeed = column[Option[Float]]("MOVINGSPEED", O.Nullable)
  def maxSpeed = column[Option[Float]]("MAXSPEED", O.Nullable)
  def maxElevation = column[Option[Float]]("MAXELEVATION", O.Nullable)
  def minElevation = column[Option[Float]]("MINELEVATION", O.Nullable)
  def ascent = column[Option[Float]]("ASCENT", O.Nullable)
  def descent = column[Option[Float]]("DESCENT", O.Nullable)
  def avgAscentRate = column[Option[Float]]("AVGASCENTRATE", O.Nullable)
  def maxAscentRate = column[Option[Float]]("MAXASCENTRATE", O.Nullable)
  def avgDescentRate = column[Option[Float]]("AVGDESCENTRATE", O.Nullable)
  def maxDescentRate = column[Option[Float]]("MAXDESCENTRATE", O.Nullable)
  def calories = column[Option[Float]]("CALORIES", O.Nullable)
  def avgHeartRate = column[Option[Float]]("AVGHEARTREATE", O.Nullable)

  override def * = (trackId, description, distance, timerTime, totalElapsedTime, movingTime, stoppedTime, movingSpeed, maxSpeed, maxElevation,
    minElevation, ascent, descent, avgAscentRate, maxAscentRate, avgDescentRate, maxDescentRate, calories, avgHeartRate) <>(TrackMetaData.tupled, TrackMetaData.unapply)

  def track = foreignKey("TRACK_FK", trackId, TableQuery[TrackTable])(_.trackId)
}

object TrackMetaDatas{



  val trackMetDataTable = TableQuery[TrackMetaDataTable]

  def insertTrackMetaData(trackMetaData: TrackMetaData)(implicit session: Session) = trackMetDataTable.insert(trackMetaData)

  def getTrackMetaDataForTrackId(trackId: UUID)(implicit session: Session): TrackMetaData = {
    trackMetDataTable.filter(_.trackId === trackId).list.head
  }

  def deleteTrackMetaData(trackId: UUID)(implicit session: Session) = {
    trackMetDataTable.filter(_.trackId === trackId).delete
  }

  def update(trackMetaData: TrackMetaData)(implicit session: Session) = {
    trackMetDataTable.filter(_.trackId === trackMetaData.trackId).update(trackMetaData)
  }
}
