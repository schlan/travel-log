package at.droelf.backend.storage

import java.nio.file._
import java.io.File
import java.util.UUID

import play.api.Play

class FileStorageService {

  val tracksDir = Play.current.configuration.getString("at.droelf.travel-log.tracks.rootDir").get
  val imageRootDir = Play.current.configuration.getString("at.droelf.travel-log.images.rootDir").get

  Files.createDirectories(Paths.get(tracksDir))
  Files.createDirectories(Paths.get(imageRootDir))

  def getAllSavedTracks = {
    recursiveListFiles(Paths.get(tracksDir).toFile)
  }

  private def recursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }


  def saveGpxTrackFile(file: File): Path = {
    Files.write(Paths.get(imageRootDir, UUID.randomUUID().toString), Files.readAllBytes(file.toPath))
  }


  def saveImageToDisk(file: File, date: String, name: String): Path = {
    Files.write(Paths.get(imageRootDir, date + "_" + name + "_" + UUID.randomUUID().toString), Files.readAllBytes(file.toPath))
  }


  def getImageFromDisk(name: String): Option[File] = {
    val path = Paths.get(imageRootDir,name)
    if(Files.exists(path)){
      Some(path.toFile)
    } else {
      None
    }
  }


}
