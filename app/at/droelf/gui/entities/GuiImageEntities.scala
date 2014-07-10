package at.droelf.gui.entities

import at.droelf.backend.DateTimeUtil
import models.Image
import org.joda.time.{DateTimeZone, DateTime}


case class GuiImage(name: String, dateTime: DateTime, path: String, location: GuiImageLocation)

object GuiImage extends DateTimeUtil{
  def apply(image: Image, location: GuiImageLocation): GuiImage = GuiImage(image.name, utcWithTimeZoneToDateTime(image.dateTime, image.dateTimeZone), image.path, location)
}

case class GuiImageLocation(latitude: Float, longitude: Float)