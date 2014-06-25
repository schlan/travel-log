package at.droelf

import controllers._
import play.api.GlobalSettings
import at.droelf.backend.storage.{FileStorageService, DBStorageService}
import at.droelf.backend.service.{UserService, TripService, GpxTrackService}

object Global extends GlobalSettings {

  lazy val fileStorageService = new FileStorageService
  lazy val dbStorageService = new DBStorageService
  lazy val gpxTrackService = new GpxTrackService(fileStorageService, dbStorageService)
  lazy val tripService = new TripService(dbStorageService)
  lazy val userService = new UserService(dbStorageService)

  lazy val controllerSingletons = Map[Class[_], AnyRef](
    (classOf[Application] -> new Application(gpxTrackService)),
    (classOf[TracksController] -> new TracksController(gpxTrackService)),
    (classOf[TripController] -> new TripController(tripService)),
    (classOf[Auth] -> new Auth(userService)),
    (classOf[AdminController] -> new AdminController())
  )

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    controllerSingletons(controllerClass).asInstanceOf[A]
  }
}