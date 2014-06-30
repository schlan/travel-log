package play.api.db.slick.joda
import com.github.tototoshi.slick.GenericJodaSupport
object PlayJodaSupport extends GenericJodaSupport(play.api.db.slick.Config.driver)