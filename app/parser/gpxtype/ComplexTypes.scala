package parser.gpxtype

import scala.xml.{Node, Elem}


trait GPXTypeDecoder[K] {
  def decode(rawData: Node): K
}


// Email

class GPXTypeEmail(var id: String, var domain: String) {
  override def toString() = "Email: " + id + "@" + domain
}

object GPXTypeEmailDecoder extends GPXTypeDecoder[GPXTypeEmail] with DecoderUtils {
  def decode(rawData: Node): GPXTypeEmail = {

    val gpxTypeEmail = new GPXTypeEmail(
      textFromNode(rawData ,"@id"),
      textFromNode(rawData, "@domain")
    )

    return gpxTypeEmail
  }

}

// Link

class GPXTypeLink(var href: String, var text: Option[String] = None, var typee: Option[String] = None) {
  override def toString() = "Link: " + href + ", text: " + text + ", type: " + typee
}

object GPXTypeLinkDecoder extends GPXTypeDecoder[GPXTypeLink] with DecoderUtils{

  def decode(rawData: Node): GPXTypeLink = {
    val gpxLink = new GPXTypeLink((rawData \ "@href").text)

    rawData.child.foreach(
      _ match{
        case <text>{data}</text> => gpxLink.text = nodeAsTextOption(data)
        case <type>{data}</type> => gpxLink.typee = nodeAsTextOption(data)
        case _ =>
      }
    )
    return gpxLink
  }
}

// Person

class GPXTypePerson(var name: Option[String] = None, var email: Option[GPXTypeEmail] = None, var link: Option[GPXTypeLink] = None) {
  override def toString() = "Name: " + name + ", Link: " + link + ", Email: " + email
}

object GPXTypePersonDecoder extends GPXTypeDecoder[GPXTypePerson] with DecoderUtils {
  def decode(rawData: Node): GPXTypePerson = {

    val gpxTypePerson = new GPXTypePerson()

    rawData.child.foreach(
      _ match {
        case <name>{data}</name> => gpxTypePerson.name = nodeAsTextOption(data)
        case elem @ <email>{_*}</email> => gpxTypePerson.email = Some(GPXTypeEmailDecoder.decode(elem))
        case elem @ <link>{_*}</link> => gpxTypePerson.link = Some(GPXTypeLinkDecoder.decode(elem))
        case _ =>
      }
    )

    return gpxTypePerson
  }
}

// Copyright

class GPXTypeCopyright(var author: String, var year : Option[String] = None, var license : Option[String] = None){
  override def toString() = "Author: " + author + " Year: " + year + " License: " + license
}

object GPXTypeCopyRightDecoder extends GPXTypeDecoder[GPXTypeCopyright] with DecoderUtils{

  def decode(rawData: Node): GPXTypeCopyright = {

    val gpxTypeCopyRight = new GPXTypeCopyright((rawData \ "@author").text)

    rawData.child.foreach(
      _ match {
        case <year>{data}</year> => gpxTypeCopyRight.year = nodeAsTextOption(data)
        case <license>{data}</license> => gpxTypeCopyRight.license = nodeAsTextOption(data)
        case _ =>
      }
    )
    return gpxTypeCopyRight
  }

}

// Bounds

class GPXTypeBounds(var minLat : Float, var minLon : Float, var maxLat : Float, var maxLon : Float){
  override def toString() =  "MinLat: " + minLat + " MinLon: " + minLon + " MaxLat: " + maxLat + " MaxLon: " + maxLon
}

object GPXTypeBoundsDecoder extends GPXTypeDecoder[GPXTypeBounds] with DecoderUtils{
  def decode(rawData : Node) : GPXTypeBounds = {

    return new GPXTypeBounds(
      floatFromNode(rawData, "@minlat"),
      floatFromNode(rawData, "@minlon"),
      floatFromNode(rawData, "@maxlat"),
      floatFromNode(rawData, "@maxlon")
    )
  }
}



