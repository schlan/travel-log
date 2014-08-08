package at.droelf.backend.storage.database

import at.droelf.Config
import play.api.Play

import scala.slick.driver.{H2Driver, JdbcProfile, PostgresDriver}

object SlickDBDriver {

  val TEST = "test"
  val DEV = "dev"
  val PROD = "prod"

  def getDriver: JdbcProfile = {
    Config.mode match {
      case Some(TEST) => H2Driver
      case Some(DEV) => H2Driver
      case Some(PROD) => PostgresDriver
      case _ => H2Driver
    }
  }
}