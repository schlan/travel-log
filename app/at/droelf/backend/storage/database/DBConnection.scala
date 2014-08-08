package at.droelf.backend.storage.database

import at.droelf.Config
import play.api.Play
import play.api.db.slick.Profile
import scala.slick.driver.JdbcProfile

class DBConnection(override val profile: JdbcProfile) extends Profile {
  import profile.simple._

  def dbObject(): Database = {

    val config = Play.current.configuration;

    val env = Config.mode.getOrElse("dev")
    val url = config.getString("db.default.url").get
    val username = config.getString("db.default.user").get
    val password = config.getString("db.default.password").get
    val driver = config.getString("db.default.driver").get

    Database.forURL(url, username, password, null, driver)
  }
}