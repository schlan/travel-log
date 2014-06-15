package parser.gpxtype

import scala.xml.{XML, NodeSeq, Node, Elem}
import scala.collection.mutable.ListBuffer
import parser.extensions.track.{TrackExtensionDecoder, TrackStatsExtensionDecoder}
import java.io.File


trait DecoderUtils{

  def nodeAsText(node : Node) = node.text
  def nodeAsFloat(node : Node) = node.text.toFloat

  def nodeSeqAsText(node : NodeSeq) = node.text
  def nodeSeqAsFloat(node : NodeSeq) = node.text.toFloat

  def nodeAsFloatOption(node : Node) = Some(nodeAsFloat(node))
  def nodeAsTextOption(node : Node) = Some(nodeAsText(node))

  def textFromNode(node : Node, name : String) = nodeSeqAsText((node \ name))
  def floatFromNode(node : Node, name : String) = nodeSeqAsFloat((node \ name))

  def textOptionFromNode(node : Node, name : String) = Some(nodeSeqAsText((node \ name)))
  def floatOptionFromNode(node : Node, name : String) = Some(nodeSeqAsFloat((node \ name)))

}

trait GPXTopNodeDecoder[K] {
  def decode(rawData: Node): K
}

object GPXMetadataDecoder extends GPXTopNodeDecoder[GPXMetadata] with DecoderUtils {

  def decode(rawData: Node): GPXMetadata = {
    val gpxMetadata = new GPXMetadata()
    rawData.child.foreach(
      _ match {
          case <name>{data}</name> => gpxMetadata.name = nodeAsTextOption(data)
          case <desc>{data}</desc> => gpxMetadata.desc = nodeAsTextOption(data)
          case elem @ <author>{_*}</author> => gpxMetadata.author = Some(GPXTypePersonDecoder.decode(elem))
          case elem @ <copyright>{_*}</copyright> => gpxMetadata.copyright = Some(GPXTypeCopyRightDecoder.decode(elem))
          case elem @ <link>{_*}</link> => gpxMetadata.link = Some(GPXTypeLinkDecoder.decode(elem))
          case <time>{data}</time> => gpxMetadata.time = nodeAsTextOption(data)
          case <keywords>{data}</keywords> => gpxMetadata.keywords = nodeAsTextOption(data)
          case elem @ <bounds>{_*}</bounds> => gpxMetadata.bounds = Some(GPXTypeBoundsDecoder.decode(elem))
          case elem @ <extension>{_*}</extension> => gpxMetadata.extension = Some(elem)
          case _ =>
        }
    )
    return gpxMetadata
  }
}

object GPXWayPointDecoder extends GPXTopNodeDecoder[GPXWayPoint] with DecoderUtils {

  def decode(rawData: Node): GPXWayPoint = {

    val gpxWayPoint = new GPXWayPoint(floatFromNode(rawData, "@lat"),floatFromNode(rawData, "@lon"))

    rawData.child.foreach(
    _ match {
        case <ele>{data}</ele> => gpxWayPoint.ele = nodeAsFloatOption(data)
        case <time>{data}</time> => gpxWayPoint.time = nodeAsTextOption(data)
        case <magvar>{data}</magvar> => gpxWayPoint.magvar = nodeAsFloatOption(data)
        case <geoidheight>{data}</geoidheight> => gpxWayPoint.geoIdHeight = nodeAsFloatOption(data)
        case <name>{data}</name> => gpxWayPoint.name = nodeAsTextOption(data)
        case <cmt>{data}</cmt> => gpxWayPoint.cmt = nodeAsTextOption(data)
        case <desc>{data}</desc> => gpxWayPoint.desc = nodeAsTextOption(data)
        case <src>{data}</src> => gpxWayPoint.src = nodeAsTextOption(data)
        case elem @ <link>{_*}</link> => gpxWayPoint.link = Some(GPXTypeLinkDecoder.decode(elem))
        case <sym>{data}</sym> => gpxWayPoint.sym = nodeAsTextOption(data)
        case <type>{data}</type> => gpxWayPoint.typee = nodeAsTextOption(data)
        case <fix>{data}</fix> => gpxWayPoint.fix = nodeAsTextOption(data)
        case <sat>{data}</sat> => gpxWayPoint.sat = nodeAsFloatOption(data)
        case <hdop>{data}</hdop> => gpxWayPoint.hdop = nodeAsFloatOption(data)
        case <vdop>{data}</vdop> => gpxWayPoint.vdop = nodeAsFloatOption(data)
        case <pdop>{data}</pdop> => gpxWayPoint.pdop = nodeAsFloatOption(data)
        case <ageofdgpsdata>{data}</ageofdgpsdata> => gpxWayPoint.ageOfDGPSData = nodeAsFloatOption(data)
        case <dgpsid>{data}</dgpsid> => gpxWayPoint.dGPSId = nodeAsFloatOption(data)
        case elem @ <extension>{_*}</extension> => gpxWayPoint.extension = Some(elem)
        case _ =>

    })
    return gpxWayPoint
  }
}

object GPXRouteDecoder extends GPXTopNodeDecoder[GPXRoute] with DecoderUtils{

  def decode(rawData: Node): GPXRoute = {

    val gpxRoute = new GPXRoute

    val wayPointList : ListBuffer[GPXWayPoint] = ListBuffer()

    rawData.child.foreach(
      _ match {
        case <name>{data}</name> => gpxRoute.name = nodeAsTextOption(data)
        case <cmt>{data}</cmt> => gpxRoute.cmt = nodeAsTextOption(data)
        case <desc>{data}</desc> => gpxRoute.desc = nodeAsTextOption(data)
        case <src>{data}</src> => gpxRoute.desc = nodeAsTextOption(data)
        case elem @ <link>{_*}</link> => gpxRoute.link = Some(GPXTypeLinkDecoder.decode(elem))
        case <number>{data}</number> => gpxRoute.number = nodeAsFloatOption(data)
        case <type>{data}</type> => gpxRoute.typee = nodeAsTextOption(data)
        case elem @ <rtept>{_*}</rtept> => wayPointList += GPXWayPointDecoder.decode(elem)
        case elem @ <extensions>{_*}</extensions> => gpxRoute.extension = Some(elem)
        case _ =>
      }
    )

    gpxRoute.routePoints = wayPointList.toList

    return gpxRoute
  }


}


object GPXTrackDecoder extends GPXTopNodeDecoder[GPXTrack] with DecoderUtils{

  def decode(rawData : Node ) : GPXTrack = {

    val gpxTrack : GPXTrack = new GPXTrack
    val trackSegmentList : ListBuffer[GPXTrackSegment] = ListBuffer()

    rawData.child.foreach(
      _ match{

        case <name>{data}</name> => gpxTrack.name = nodeAsTextOption(data)
        case <cmt>{data}</cmt> => gpxTrack.cmt = nodeAsTextOption(data)
        case <desc>{data}</desc> => gpxTrack.desc = nodeAsTextOption(data)
        case <src>{data}</src> => gpxTrack.desc = nodeAsTextOption(data)
        case elem @ <link>{_*}</link> => gpxTrack.link = Some(GPXTypeLinkDecoder.decode(elem))
        case <number>{data}</number> => gpxTrack.number = nodeAsFloatOption(data)
        case <type>{data}</type> => gpxTrack.typee = nodeAsTextOption(data)
        case elem @ <extensions>{_*}</extensions> => gpxTrack.extension = Some(elem); gpxTrack.trackStatsExtension = TrackStatsExtensionDecoder.decode(elem); gpxTrack.trackExtension = TrackExtensionDecoder.decode(elem)
        case elem @ <trkseg>{_*}</trkseg> => trackSegmentList += GPXTrackSegmentDecoder.decode(elem)
        case _ =>
      }
    )

    gpxTrack.trackSegments = trackSegmentList.toList
    return gpxTrack
  }

}

object GPXTrackSegmentDecoder extends GPXTopNodeDecoder[GPXTrackSegment] with DecoderUtils{

  def decode(rawData : Node) : GPXTrackSegment ={

    val gpxTrackSegment : GPXTrackSegment = new GPXTrackSegment
    val gpxTrackPointList : ListBuffer[GPXWayPoint] = ListBuffer()

    rawData.child.foreach(
      _ match {
        case elem @ <trkpt>{_*}</trkpt> => gpxTrackPointList += GPXWayPointDecoder.decode(elem)
        case elem @ <extension>{_*}</extension> => gpxTrackSegment.extension = Some(elem)
        case _ =>
      }
    )

    gpxTrackSegment.trackPoints = gpxTrackPointList.toList
    return gpxTrackSegment
  }

}

object GPXDecoder extends DecoderUtils{

  def decodeFile(filePath : File) = decode(loadFile(filePath))


  def decode(rawData : Elem) : GPX = {

    val gpx : GPX = new GPX(textFromNode(rawData,"@version"), textFromNode(rawData,"@creator"))

    val waypoints : ListBuffer[GPXWayPoint] = ListBuffer()
    val routes : ListBuffer[GPXRoute] = ListBuffer()
    val tracks : ListBuffer[GPXTrack] = ListBuffer()

    rawData match  {

      case <gpx>{data @ _*}</gpx> =>
        data.foreach(
          _ match {
            case elem @ <metadata>{_*}</metadata> => gpx.metadata = Some(GPXMetadataDecoder.decode(elem))
            case elem @ <wpt>{_*}</wpt> => waypoints += GPXWayPointDecoder.decode(elem)
            case elem @ <rte>{_*}</rte> => routes += GPXRouteDecoder.decode(elem)
            case elem @ <trk>{_*}</trk> => tracks += GPXTrackDecoder.decode(elem)
            case elem @ <extension>{_*}</extension> => gpx.extensions = Some(elem)
            case _ =>
          }
        )
    }

    gpx.tracks = tracks.toList
    gpx.routes = routes.toList
    gpx.waypoints = waypoints.toList
    return gpx
  }


  def loadFile(filePath: File): Elem = {
    return XML.loadFile(filePath)
  }

}

