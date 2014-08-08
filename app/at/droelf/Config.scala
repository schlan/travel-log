package at.droelf

import play.api.Play


object Config {

  val imagePath = getConfigValueString("at.droelf.travel-log.images.rootDir")
  val tracksPath = getConfigValueString("at.droelf.travel-log.tracks.rootDir")

  val mode = getConfigValueString("at.droelf.travel-log.db.mode")

  val user = getConfigValueString("at.droelf.travel-log.user")
  val password = getConfigValueString("at.droelf.travel-log.password")

  val analytics = getConfigValueString("at.droelf.travel-log.analytics")

  val emailRecipient = getConfigValueString("at.droelf.travel-log.email.recipient")
  val emailSender = getConfigValueString("at.droelf.travel-log.email.sender")
  val emailHold = getConfigValueInt("at.droelf.travel-log.email.hold")

  private def getConfigValueString(key: String) = Play.current.configuration.getString(key)
  private def getConfigValueInt(key: String) = Play.current.configuration.getInt(key)


}
