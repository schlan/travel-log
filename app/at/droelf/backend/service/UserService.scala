package at.droelf.backend.service

import at.droelf.backend.storage.DBStorageService
import models.User

class UserService(dbStorage: DBStorageService) {
  //FIXME

  def checkUserPassword(user: String, password: String): Boolean = {
    (user== "admin" && password == "1234")

  }

  def findUserByName(userName: String): Option[User] = ???

}
