package models

import com.github.tototoshi.slick.H2JodaSupport._
import play.api.db.slick.Config.driver.simple._
import org.joda.time._

case class Image(name: String, dateTime: LocalDateTime, dateTimeZone: DateTimeZone, path: String)

class ImageTable(tag: Tag) extends Table[Image](tag, "IMAGES"){

  def name = column[String]("NAME", O.NotNull)
  def dateTime = column[LocalDateTime]("DATETIME", O.NotNull)
  def dateTimeZone = column[DateTimeZone]("DATETIME_ZONE", O.NotNull)
  def path = column[String]("PATH")

  def * = (name, dateTime, dateTimeZone, path) <> (Image.tupled, Image.unapply)

}

object Images{

  val imageTable = TableQuery[ImageTable]

  def insertImage(image: Image)(implicit session: Session) = {
    imageTable.insert(image)
  }

  def getImagesByLocalDate(date: LocalDate)(implicit session: Session): Seq[Image] = {
    val start = date.toLocalDateTime(LocalTime.MIDNIGHT)
    val end = date.plusDays(1).toLocalDateTime(LocalTime.MIDNIGHT)

    imageTable.filter(e => ((e.dateTime >= start) && e.dateTime < end)).list
  }

}
