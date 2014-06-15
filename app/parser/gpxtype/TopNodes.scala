package parser.gpxtype

import scala.xml.{Node, NodeSeq}
import parser.extensions.track.{TrackExtension, TrackStatsExtension}

class GPX(var version: String,
          var creator: String,
          var metadata: Option[GPXMetadata] = None,
          var waypoints: List[GPXWayPoint] = List(),
          var routes: List[GPXRoute] = List(),
          var tracks: List[GPXTrack] = List(),
          var extensions: Option[Node] = None) {

  override def toString() =
    "Version: " + version +
    "\n\nCreator: " + creator +
    "\n\nMetadata: " + metadata +
    "\n\nWayPoints: " + waypoints.foldLeft("")(_ + " " + _)+
    "\n\nRoutes: " + routes.foldLeft("")(_ + " " + _) +
    "\n\nTracks: " + tracks.foldLeft("")(_ + " " + _)
}

class GPXMetadata(
                   var name: Option[String] = None,
                   var desc: Option[String] = None,
                   var author: Option[GPXTypePerson] = None,
                   var copyright: Option[GPXTypeCopyright] = None,
                   var link: Option[GPXTypeLink] = None,
                   var time: Option[String] = None,
                   var keywords: Option[String] = None,
                   var bounds: Option[GPXTypeBounds] = None,
                   var extension: Option[Node] = None
                   ) {

  override def toString() = "File Name: " + name + "\nDesc.: " + desc + "\nAuthor: " + author + "\nLink:" + link + "\nCopyright: " + copyright + "\nBounds" + bounds

}

class GPXWayPoint(var latitude: Float,
                  var longitude: Float,
                  var ele: Option[Float] = None,
                  var time: Option[String] = None,
                  var magvar: Option[Float] = None,
                  var geoIdHeight: Option[Float] = None,
                  var name: Option[String] = None,
                  var cmt: Option[String] = None,
                  var desc: Option[String] = None,
                  var src: Option[String] = None,
                  var link: Option[GPXTypeLink] = None,
                  var sym: Option[String] = None,
                  var typee: Option[String] = None,
                  var fix: Option[String] = None,
                  var sat: Option[Float] = None,
                  var hdop: Option[Float] = None,
                  var vdop: Option[Float] = None,
                  var pdop: Option[Float] = None,
                  var ageOfDGPSData: Option[Float] = None,
                  var dGPSId: Option[Float] = None,
                  var extension: Option[Node] = None) {

  override def toString() = "Latitude: " + latitude + "\nLongitude: " + longitude + "\ntime " + time + "\nname " + name + "\nsym " + sym
}

class GPXRoute(var name: Option[String] = None,
               var cmt: Option[String] = None,
               var desc: Option[String] = None,
               var src: Option[String] = None,
               var link: Option[GPXTypeLink] = None,
               var number: Option[Float] = None,
               var typee: Option[String] = None,
               var routePoints: List[GPXWayPoint] = List(),
               var extension: Option[Node] = None) {

  override def toString() = "Name: " + name + ", #TrackPoints: " + routePoints.size

}

class GPXTrack(var name: Option[String] = None,
               var cmt: Option[String] = None,
               var desc: Option[String] = None,
               var src: Option[String] = None,
               var link: Option[GPXTypeLink] = None,
               var number: Option[Float] = None,
               var typee: Option[String] = None,
               var trackSegments: List[GPXTrackSegment] = List(),
               var trackStatsExtension: Option[TrackStatsExtension] = None,
               var trackExtension: Option[TrackExtension] = None,
               var extension: Option[Node] = None) {

  override def toString() = "Name: " + name + ", #TrackSegments: " + trackSegments.size + ", " + trackSegments

}

class GPXTrackSegment(var trackPoints: List[GPXWayPoint] = List(),
                      var extension: Option[Node] = None) {
  override def toString() = "#Trackpoints: " + trackPoints.size

}