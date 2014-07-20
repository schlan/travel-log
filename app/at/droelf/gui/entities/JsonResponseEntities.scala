package at.droelf.gui.entities


case class DayTourInformationResponse(
                                       tracks:                Seq[GuiTrack],
                                       summarizedMetaData:    Option[GuiTrackMetaData],
                                       images:                Seq[GuiImage]
                                       )

case class TripSummaryResponse(
                                lastKnownPosition:              Option[GuiTrackPoint],
                                summarizedMetaDataByActivity:   Map[String,GuiTrackMetaData],
                                condensedTracks:                Seq[GuiTrack],
                                newestImages:                   Seq[GuiImage]
                               )