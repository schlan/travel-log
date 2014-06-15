package models

import com.github.tototoshi.slick.H2JodaSupport._
import play.api.db.slick.Config.driver.simple._
import org.joda.time.DateTime

case class Photo(name: String, description: String, dateTime: DateTime, latitude: Float, longitude: Float, path: String)

class PhotoTable(tag: Tag) extends Table[Photo](tag, "PHOTO"){

  def name = column[String]("NAME", O.NotNull)
  def description = column[String]("DESCRIPTION")
  def dateTime = column[DateTime]("DATETIME")
  def latitude = column[Float]("LATITUDE")
  def longitude = column[Float]("LONGITUDE")
  def path = column[String]("PATH")

  def * = (name, description, dateTime, latitude, longitude, path) <> (Photo.tupled, Photo.unapply)

}
