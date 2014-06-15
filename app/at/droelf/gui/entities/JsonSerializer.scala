package at.droelf.gui.entities

import play.api.libs.json.{JsValue, Json, Writes}


trait JsonSerializer {


  implicit val guiTrackMetadataWrites = new Writes[GuiTrackMetaData] {
    override def writes(o: GuiTrackMetaData): JsValue = Json.obj(
      "description" -> o.description,
      "distance" -> o.displayColor,
      "timerTime" -> o.timerTime,
      "totalElapsedTime" -> o.totalElapsedTime,
      "movingTime" -> o.movingTime,
      "stoppedTime" -> o.stoppedTime,
      "movingSpeed" -> o.movingSpeed,
      "maxSpeed" -> o.maxSpeed,
      "maxElevation" -> o.maxElevation,
      "minElevation" -> o.minElevation,
      "ascent" -> o.ascent,
      "descent" -> o.descent,
      "avgAscentRate" -> o.avgAscentRate,
      "maxAscentRate" -> o.maxAscentRate,
      "avgDescentRate" -> o.avgDescentRate,
      "maxDescentRate" -> o.maxDescentRate,
      "calories" -> o.calories,
      "avgHeartRate" -> o.avgHeartRate,
      "avgCadence" -> o.avgCadence,
      "displayColor" -> o.displayColor
    )
  }

  implicit val guiTrackPoint = new Writes[GuiTrackPoint] {
    override def writes(o: GuiTrackPoint): JsValue = Json.obj(
      "datetime" -> o.dateTime.toString(),
      "elevation" -> o.elevation,
      "longitude" -> o.longitude,
      "latitude" -> o.latitude
    )
  }

  implicit val guiTrackWrites = new Writes[GuiTrack] {
    override def writes(o: GuiTrack): JsValue = Json.obj(
      "name" -> o.name,
      "trackId" -> o.trackId.toString,
      "metadata" -> o.metaData,
      "trackPoints" -> o.trackPoints
    )
  }

}
