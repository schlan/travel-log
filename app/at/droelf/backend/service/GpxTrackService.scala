package at.droelf.backend.service

import java.io.File
import java.util.UUID

import at.droelf.backend.storage.FileStorageService
import at.droelf.backend.storage.database.DBStorageService
import at.droelf.gui.entities.{GuiTrack, GuiTrackMetaData}
import models.{TrackMetaData, TrackPoint}
import org.joda.time.{DateTimeZone, LocalDate}
import parser.gpxtype.GPXDecoder

class GpxTrackService(fileStorageService: FileStorageService, dbTrackStorageService: DBStorageService) {

  // Put
  def saveTracks(file: File, activity: String, timeZoneOffset: DateTimeZone) {
    val x = GPXDecoder.decodeFile(file)
    fileStorageService.saveGpxTrackFile(file)
    dbTrackStorageService.saveTracks(x.tracks, timeZoneOffset, activity)
  }


  // Get
  def getAllTracks(): Seq[GuiTrack] = {
    dbTrackStorageService.getAllTracks().map(trk => GuiTrack(trk, getTrackMetaDataForTrackId(trk.trackId), getTrackPointsForTrackId(trk.trackId)))
  }

  def getTrackById(trackId: UUID): Option[GuiTrack] = {
    dbTrackStorageService.getTrackById(trackId).map(trk => GuiTrack(trk, getTrackMetaDataForTrackId(trk.trackId), getTrackPointsForTrackId(trk.trackId)))
  }

  def getTracksForLocalDate(date: LocalDate): Seq[GuiTrack] = dbTrackStorageService.getTrackByDate(date).map(trk => GuiTrack(trk, getTrackMetaDataForTrackId(trk.trackId), getTrackPointsForTrackId(trk.trackId)))

  def getTrackInformationForLocalDate(date: LocalDate): (Seq[GuiTrack], Option[GuiTrackMetaData]) = {
    val tracks = getTracksForLocalDate(date)
    tracks.size match{
      case 0 => (tracks, None)
      case _ => (tracks, Some(tracks.map(_.metaData).reduceLeft[GuiTrackMetaData]((total: GuiTrackMetaData, cur: GuiTrackMetaData) => (total + cur))))
    }
  }

  private def getTrackMetaDataForTrackId(trackId: UUID): TrackMetaData = dbTrackStorageService.getMetaDataForTrackId(trackId)

  private def getTrackPointsForTrackId(trackId: UUID): Seq[TrackPoint] = dbTrackStorageService.getAllTrackPointsForTrackId(trackId)

}
