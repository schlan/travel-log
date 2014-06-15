package models
import play.api.db.slick.Config.driver.simple._
import java.util.UUID

case class DayTourMetaData(dayTourId: UUID, description: String, weatherCond: String, roadCond: String)

class DayTourMetaDataTable(tag: Tag) extends Table[DayTourMetaData](tag, "DAYTOUR_METADATA"){

  def dayTourId = column[UUID]("DAYTOUR_ID", O.PrimaryKey)
  def description = column[String]("DESCRIPTION")
  def weatherCond = column[String]("WEATHER_COND")
  def roadCond = column[String]("ROAD_COND")

  override def * = (dayTourId, description, weatherCond, roadCond) <> (DayTourMetaData.tupled, DayTourMetaData.unapply)

  def dayTour = foreignKey("DAYTOUR_METADATA_FK", dayTourId, TableQuery[DayTourTable])(_.dayTourId)
}