package at.droelf

import at.droelf.backend.storage.database.DBStorageService
import controllers._
import play.api.GlobalSettings
import at.droelf.backend.storage.FileStorageService
import at.droelf.backend.service._

object Global extends GlobalSettings {

  lazy val fileStorageService = new FileStorageService
  lazy val dbStorageService = new DBStorageService
  lazy val gpxTrackService = new GpxTrackService(fileStorageService, dbStorageService)
  lazy val tripService = new TripService(dbStorageService)
  lazy val userService = new UserService(dbStorageService)
  lazy val adminService = new AdminService(dbStorageService)
  lazy val timeToLocationService = new TimeToLocationService(gpxTrackService)
  lazy val imageService = new ImageService(fileStorageService,dbStorageService, timeToLocationService)


  lazy val controllerSingletons = Map[Class[_], AnyRef](
    (classOf[Application] -> new Application(gpxTrackService)),
    (classOf[TracksController] -> new TracksController(gpxTrackService)),
    (classOf[TripController] -> new TripController(tripService,imageService)),
    (classOf[AuthController] -> new AuthController(userService)),
    (classOf[AdminController] -> new AdminController(userService,adminService)),
    (classOf[ImageController] -> new ImageController(imageService, userService)),
    (classOf[JsonController] -> new JsonController(gpxTrackService, imageService, tripService))
  )

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    controllerSingletons(controllerClass).asInstanceOf[A]
  }
}