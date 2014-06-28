package at.droelf.backend.service

import java.io.File
import play.api.Play
import java.nio.file.{Path, Files, Paths}
import java.util.UUID

class ImageService {

  // TODO imageFileStorage
  val imageRootDir = Play.current.configuration.getString("at.droelf.travel-log.images.rootDir").get

  // create if not exists
  Files.createDirectories(Paths.get(imageRootDir))

  def saveImage(file: File, date: String, name: String) {
    Files.write(Paths.get(imageRootDir, date + "_" + name + "_" + UUID.randomUUID().toString), Files.readAllBytes(file.toPath))
  }
}
