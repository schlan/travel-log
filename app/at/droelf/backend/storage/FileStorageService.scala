package at.droelf.backend.storage

import java.nio.file._
import java.io.File
import java.util.UUID

import at.droelf.Config
import play.api.Play
import play.api.libs.Files.TemporaryFile

class FileStorageService {

  val tracksDir = Config.tracksPath.get
  val imageRootDir = Config.imagePath.get

  Files.createDirectories(Paths.get(tracksDir))
  Files.createDirectories(Paths.get(imageRootDir))

  def getAllSavedTracks = {
    recursiveListFiles(Paths.get(tracksDir).toFile)
  }

  private def recursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }


  def saveGpxTrackFile(file: TemporaryFile): Path = {
    val finalPath = Paths.get(tracksDir, UUID.randomUUID().toString)
    file.moveTo(finalPath.toFile)
    finalPath
  }


  def saveImageToDisk(file: TemporaryFile, date: String, name: String): Path = {
    val finalPath = Paths.get(imageRootDir, date + "_" + UUID.randomUUID().toString)
    file.moveTo(finalPath.toFile)

    finalPath
  }


  def getImageFromDisk(name: String): Option[File] = {
    val path = Paths.get(imageRootDir,name)
    Files.exists(path) match {
      case true => Some(path.toFile)
      case false => None
    }
  }


}
