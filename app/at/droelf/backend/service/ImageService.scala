package at.droelf.backend.service

import java.io.File

import at.droelf.backend.DateTimeUtil
import at.droelf.backend.storage.FileStorageService
import at.droelf.backend.storage.database.DBStorageService
import at.droelf.gui.entities.GuiImage
import models.Image
import org.joda.time.{DateTime, DateTimeZone, LocalDate}
import play.api.libs.Files.TemporaryFile

class ImageService(fileStorageService: FileStorageService, dbStorage: DBStorageService, timeToLocationService: TimeToLocationService) extends DateTimeUtil{


  def saveImage(image: TemporaryFile, date: DateTime, name: String) {
    val path = fileStorageService.saveImageToDisk(image, date.toString, name)
    val localdatetime = dateTimeToUtcLocalDateTime(date)
    val timezone = date.getChronology.getZone

    dbStorage.insertImage(Image(dbStorage.getRandomId, name, localdatetime, timezone, path.getFileName.toString))
  }

  def getImagesForDate(date: LocalDate): Seq[GuiImage] = {
    dbStorage.getImagesForLocalDate(date).map(img => GuiImage(img, timeToLocationService.getLocationForDateTime(utcWithTimeZoneToDateTime(img.dateTime,img.dateTimeZone))))
  }

  def getImageFile(name: String): File = {
    fileStorageService.getImageFromDisk(name) match {
      case Some(x) => x
      case None => new File("public/images/favicon.png")
    }
  }

  def getNewestImagesForTimeRange(startDate: LocalDate, endDate: LocalDate, numberOfImages: Int = 20): Seq[GuiImage] = {
    dbStorage.getNewestImagesForTimeRange(startDate, endDate, numberOfImages).map(img => GuiImage(img, timeToLocationService.getLocationForDateTime(utcWithTimeZoneToDateTime(img.dateTime,img.dateTimeZone))))
  }

}
