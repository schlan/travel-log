package at.droelf

import play.api.data.Forms
import play.api.data.Forms._

/**
 * Created by basti on 8/8/14.
 */
trait FormTypes {

  import play.api.data.format.Formats._

  val float = Forms.of[Float]
  val optionalFloat = optional(float)
  val optionalText = optional(text)


}
