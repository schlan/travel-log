package at.droelf.backend.service

import java.io.File
import java.util.UUID

import at.droelf.backend.storage.FileStorageService
import at.droelf.backend.storage.database.DBStorageService
import at.droelf.gui.entities.{GuiTrackPoint, GuiTrack, GuiTrackMetaData}
import models.{TrackMetaData, TrackPoint}
import org.joda.time.{DateTimeZone, LocalDate}
import parser.gpxtype.GPXDecoder
import play.api.Logger

class GpxTrackService(fileStorageService: FileStorageService, dbTrackStorageService: DBStorageService) {

  // Put
  def saveTracks(file: File, activity: String, timeZoneOffset: DateTimeZone) {
    val x = GPXDecoder.decodeFile(file)
    fileStorageService.saveGpxTrackFile(file)
    dbTrackStorageService.saveTracks(x.tracks, timeZoneOffset, activity)
  }

  // Get
  def getTrackById(trackId: UUID): Option[GuiTrack] = {
    dbTrackStorageService.getTrackById(trackId).map(trk => GuiTrack(trk, getTrackMetaDataForTrackId(trk.trackId), getTrackPointsForTrackId(trk.trackId)))
  }

  def getTracksForLocalDate(date: LocalDate): Seq[GuiTrack] = dbTrackStorageService.getTrackByDate(date).map(trk => GuiTrack(trk, getTrackMetaDataForTrackId(trk.trackId), getTrackPointsForTrackId(trk.trackId)))

  def getCondensedTrackForLocalDate(date: LocalDate): Seq[GuiTrack] = {
    val lis = dbTrackStorageService.getTrackByDate(date).map{ trk =>
      GuiTrack(trk, getTrackMetaDataForTrackId(trk.trackId),getCondensedTrackPointsForTrackId(trk.trackId))
    }
    lis
  }

  def getTrackInformationForLocalDate(date: LocalDate): (Seq[GuiTrack], GuiTrackMetaData) = {
    val tracks = getTracksForLocalDate(date)
    (tracks, tracks.map(_.metaData).foldLeft(GuiTrackMetaData.empty)((m1, m2) => (m1 + m2)))
  }

  def getLatestTrackPointForDate(date: LocalDate): Option[GuiTrackPoint] = {
    val trkPt = getTracksForLocalDate(date).map(e => e.trackPoints).flatten
    trkPt.isEmpty match{
      case true => None
      case false => Some(trkPt.sortWith((p1,p2) => p1.dateTime.isAfter(p2.dateTime)).head)
    }
  }

  private def getTrackMetaDataForTrackId(trackId: UUID): TrackMetaData = dbTrackStorageService.getMetaDataForTrackId(trackId)

  private def getTrackPointsForTrackId(trackId: UUID): Seq[TrackPoint] = dbTrackStorageService.getAllTrackPointsForTrackId(trackId)

  private def getCondensedTrackPointsForTrackId(trackId: UUID): Seq[TrackPoint] = {
    dbTrackStorageService.getTrackPointsForOverview(trackId)
  }

}
