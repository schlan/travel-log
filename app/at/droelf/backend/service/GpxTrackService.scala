package at.droelf.backend.service

import java.io.File
import parser.gpxtype.GPXDecoder
import java.util.UUID
import at.droelf.backend.storage.{FileStorageService, DBStorageService}
import org.joda.time.LocalDate
import at.droelf.gui.entities.{TrackInformationResponse, GuiTrackMetaData, GuiTrack, GuiTrackPoint}
import models.{TrackMetaData, TrackPoint}

class GpxTrackService(fileStorageService: FileStorageService, dbTrackStorageService: DBStorageService) {

  // Put
  def saveTracks(file: File){
    val x = GPXDecoder.decodeFile(file)
    fileStorageService.saveGpxTrackFile(file)
    dbTrackStorageService.saveTracks(x.tracks)
  }

  // Get
  def getAllTracks(): Seq[GuiTrack] = {
    dbTrackStorageService.getAllTracks().map(trk => GuiTrack(trk, getTrackMetaDataForTrackId(trk.trackId), getTrackPointsForTrackId(trk.trackId)))
  }

  def getTrackById(trackId: UUID): Option[GuiTrack] = {
    dbTrackStorageService.getTrackById(trackId).map(trk => GuiTrack(trk, getTrackMetaDataForTrackId(trk.trackId), getTrackPointsForTrackId(trk.trackId)))
  }

  def getTracksForLocalDate(date: LocalDate): Seq[GuiTrack] = dbTrackStorageService.getTrackByDate(date).map(trk => GuiTrack(trk,getTrackMetaDataForTrackId(trk.trackId),getTrackPointsForTrackId(trk.trackId)))

  def getTrackInformationForLocalDate(date: LocalDate): TrackInformationResponse = {
    val tracks = getTracksForLocalDate(date)
    TrackInformationResponse(tracks, tracks.map(_.metaData).reduceLeft[GuiTrackMetaData]((total: GuiTrackMetaData, cur: GuiTrackMetaData) => (total + cur)))
  }

  private def getTrackMetaDataForTrackId(trackId: UUID): TrackMetaData= dbTrackStorageService.getMetaDataForTrackId(trackId)

  private def getTrackPointsForTrackId(trackId: UUID): Seq[TrackPoint] = dbTrackStorageService.getAllTrackPointsForTrackId(trackId)

}
