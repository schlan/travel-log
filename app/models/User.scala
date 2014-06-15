package models

import play.api.db.slick.Config.driver.simple._

case class User(username: String, password: String, displayName: String)

class UserTable(tag: Tag) extends Table[User](tag, "USER"){

  def username = column[String]("UNAME", O.PrimaryKey)
  def password = column[String]("PASSWORD", O.NotNull)
  def displayName = column[String]("DNAME", O.NotNull)

  def * = (username, password, displayName) <> (User.tupled, User.unapply)
}
