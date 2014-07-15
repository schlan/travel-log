package at.droelf.gui.entities


case class DayTourInformationResponse(tracks: Seq[GuiTrack], summarizedMetaData: Option[GuiTrackMetaData], images: Seq[GuiImage])

