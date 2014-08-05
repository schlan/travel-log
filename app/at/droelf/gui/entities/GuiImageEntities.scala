package at.droelf.gui.entities

import java.util.UUID

import at.droelf.backend.DateTimeUtil
import models.Image
import org.joda.time.{LocalDateTime, DateTimeZone, DateTime}


case class GuiImage(id: UUID, name: String, dateTime: DateTime, path: String, location: GuiImageLocation)

object GuiImage extends DateTimeUtil{
  def apply(image: Image, location: GuiImageLocation): GuiImage = GuiImage(image.id, image.name, utcWithTimeZoneToDateTime(image.dateTime, image.dateTimeZone), image.path, location)
}

case class GuiImageLocation(latitude: Float, longitude: Float)

object GuiImageLocation{
  def apply(): GuiImageLocation = GuiImageLocation(-1,-1)
}
