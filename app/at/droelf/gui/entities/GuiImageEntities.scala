package at.droelf.gui.entities

import models.Image
import org.joda.time.DateTime


case class GuiImage(name: String, dateTime: DateTime, path: String, location: GuiImageLocation)

object GuiImage{
  def apply(image: Image, location: GuiImageLocation): GuiImage = GuiImage(image.name, image.dateTime.toDateTime(image.dateTimeZone), image.path, location)
}

case class GuiImageLocation(latitude: Float, longitude: Float)