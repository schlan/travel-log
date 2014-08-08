package at.droelf.backend.service

import at.droelf.Config
import at.droelf.backend.storage.database.DBStorageService

class UserService(dbStorage: DBStorageService) {

  private val user = Config.user.get
  private val password = Config.password.get

  def checkUserPassword(user: String, password: String): Boolean = (user == this.user && password == this.password)

}
