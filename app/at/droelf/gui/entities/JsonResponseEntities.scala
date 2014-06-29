package at.droelf.gui.entities


case class DayTourInformationResponse(tracks: Seq[GuiTrack], summarizedMetaData: GuiTrackMetaData, images: Seq[GuiImage])

