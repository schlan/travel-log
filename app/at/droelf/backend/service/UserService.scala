package at.droelf.backend.service


import at.droelf.backend.storage.DBStorageService
import play.api.Logger

class UserService(dbStorage: DBStorageService) {

  def checkUserPassword(user: String, password: String): Boolean = {
    Logger.info(user + " " + password)
    (user== "admin" && password == "1234")
  }

}
