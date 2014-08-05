package at.droelf.backend.service

import at.droelf.backend.storage.database.DBStorageService
import models.User
import play.api.Play

class UserService(dbStorage: DBStorageService) {
  //FIXME

  private val user = Play.current.configuration.getString("at.droelf.travel-log.user").get
  private val password = Play.current.configuration.getString("at.droelf.travel-log.password").get

  def checkUserPassword(user: String, password: String): Boolean = (user == this.user && password == this.password)

}
