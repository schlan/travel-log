package at.droelf.backend.service

import at.droelf.gui.entities.{GuiTrackPoint, GuiImageLocation}
import org.joda.time.{Seconds, DateTime}

class TimeToLocationService(gpxTrackService: GpxTrackService) {


  def getLocationForDateTime(dateTime: DateTime): GuiImageLocation = {
    val track = gpxTrackService.getTracksForLocalDate(dateTime.toLocalDate)
    val wpts = track.map(t => t.trackPoints).flatten.sortWith((w1,w2) => compare(dateTime,w1,w2))
    GuiImageLocation(wpts.head.latitude, wpts.head.longitude)
  }

  private def compare(dateTime: DateTime, w1: GuiTrackPoint, w2: GuiTrackPoint): Boolean = {
    (Math.abs(Seconds.secondsBetween(w1.dateTime,dateTime).getSeconds) < Math.abs(Seconds.secondsBetween(w2.dateTime,dateTime).getSeconds))
  }

}
