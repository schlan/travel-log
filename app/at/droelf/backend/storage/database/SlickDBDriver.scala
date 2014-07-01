package at.droelf.backend.storage.database

import play.api.Play

import scala.slick.driver.{H2Driver, JdbcProfile, MySQLDriver, PostgresDriver}

object SlickDBDriver {

  val TEST = "test"
  val DEV = "dev"
  val PROD = "prod"

  def getDriver: JdbcProfile = {
    Play.current.configuration.getString("at.droelf.travel-log.db.mode") match {
      case Some(TEST) => H2Driver
      case Some(DEV) => H2Driver
      case Some(PROD) => PostgresDriver
      case _ => H2Driver
    }
  }
}