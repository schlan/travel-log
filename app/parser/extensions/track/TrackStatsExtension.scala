package parser.extensions.track

import scala.xml.{Elem, Node, NodeSeq}
import parser.gpxtype.{DecoderUtils, GPXTopNodeDecoder}


class TrackStatsExtension(
                           var distance: Option[Float] = None,
                           var timerTime: Option[Float]= None,
                           var totalElapsedTime: Option[Float]= None,
                           var movingTime: Option[Float]= None,
                           var stoppedTime: Option[Float]= None,
                           var movingSpeed: Option[Float]= None,
                           var maxSpeed: Option[Float]= None,
                           var maxElevation: Option[Float]= None,
                           var minElevation: Option[Float]= None,
                           var ascent: Option[Float]= None,
                           var descent: Option[Float]= None,
                           var avgAscentRate: Option[Float]= None,
                           var maxAscentRate: Option[Float]= None,
                           var avgDescentRate: Option[Float]= None,
                           var maxDescentRate: Option[Float]= None,
                           var calories: Option[Float]= None,
                           var avgHeartRate: Option[Float]= None,
                           var avgCadence: Option[Float]= None,
                           var extension: Option[NodeSeq] = None
                           ) {}


object TrackStatsExtensionDecoder extends GPXTopNodeDecoder[Option[TrackStatsExtension]] with DecoderUtils{

  override def decode(rawData: Node): Option[TrackStatsExtension] ={
    val trackStats = new TrackStatsExtension()

    var trackStatsRaw : Option[Node] = None
    rawData.child.foreach(
      _ match {
        case elem @ <TrackStatsExtension>{_*}</TrackStatsExtension> => trackStatsRaw = Some(elem)
        case _ =>
      }
    )

    println(trackStatsRaw.get.getClass)

    trackStatsRaw match{
      case Some(x) => x.child.foreach(
        _ match {
          case <Distance>{data}</Distance> => trackStats.distance = nodeAsFloatOption(data)
          case <TimerTime>{data}</TimerTime> => trackStats.timerTime = nodeAsFloatOption(data)
          case <TotalElapsedTime>{data}</TotalElapsedTime> => trackStats.totalElapsedTime = nodeAsFloatOption(data)
          case <MovingTime>{data}</MovingTime> => trackStats.movingTime = nodeAsFloatOption(data)
          case <StoppedTime>{data}</StoppedTime> => trackStats.stoppedTime = nodeAsFloatOption(data)
          case <MovingSpeed>{data}</MovingSpeed> => trackStats.movingSpeed = nodeAsFloatOption(data)
          case <MaxSpeed>{data}</MaxSpeed> => trackStats.maxSpeed = nodeAsFloatOption(data)
          case <MaxElevation>{data}</MaxElevation> => trackStats.maxElevation = nodeAsFloatOption(data)
          case <MinElevation>{data}</MinElevation> => trackStats.minElevation = nodeAsFloatOption(data)
          case <Ascent>{data}</Ascent> => trackStats.ascent = nodeAsFloatOption(data)
          case <Descent>{data}</Descent> => trackStats.descent = nodeAsFloatOption(data)
          case <AvgAscentRate>{data}</AvgAscentRate> => trackStats.avgAscentRate = nodeAsFloatOption(data)
          case <MaxAscentRate>{data}</MaxAscentRate> => trackStats.maxAscentRate = nodeAsFloatOption(data)
          case <AvgDescentRate>{data}</AvgDescentRate> => trackStats.avgDescentRate = nodeAsFloatOption(data)
          case <MaxDescentRate>{data}</MaxDescentRate> => trackStats.maxDescentRate = nodeAsFloatOption(data)
          case <Calories>{data}</Calories> => trackStats.calories = nodeAsFloatOption(data)
          case <AvgHeartRate>{data}</AvgHeartRate> => trackStats.avgHeartRate= nodeAsFloatOption(data)
          case <AvgCadence>{data}</AvgCadence> => trackStats.avgCadence = nodeAsFloatOption(data)
          case elem @ <Extensions>{_*}</Extensions> => trackStats.extension = Some(elem)
          case _ =>
        }
      )
      case None => return None
    }

    return Some(trackStats)
  }
}