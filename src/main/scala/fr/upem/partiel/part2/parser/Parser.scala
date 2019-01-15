package fr.upem.partiel.part2.parser

import fr.upem.partiel.part2.model.Movie
import fr.upem.partiel.part2.model.Movie._
import play.api.libs.functional.syntax._
import play.api.libs.json._

import scala.util.Try

object Parser {

  // TODO
  def toDirector: String => Option[Director] = s => Try {
    val Array(fn, ln) = s.split(" ")
    Director(fn, ln)
  }.toOption

  // TODO
  def toName: String => Title = Title.apply

  // TODO
  def toCountry: String => Option[Country] = {
    case "FR" => Some(Country.France)
    case "UK" => Some(Country.England)
    case "GE" => Some(Country.Germany)
    case "IT" => Some(Country.Italy)
    case "US" => Some(Country.UnitedStates)
    case _ => Option.empty
  }

  // TODO
  def toYear: String => Option[Year] = s => Try(s.toInt).filter(i => i > 999 && i < 3000).map(Year.apply).toOption

  // TODO
  def toViews: BigDecimal => Option[Views] = v => Try(v.toLongExact).map(Views.apply).toOption

  implicit val directorReads = Reads[Director] {
    case JsString(value) => toDirector(value).map(JsSuccess(_)).getOrElse(JsError("Not a valid Director"))
    case _ => JsError("Not a valid type for Director")
  }

  implicit val nameReads = Reads[Title] {
    case JsString(value) => JsSuccess(toName(value))
    case _ => JsError("Not a valid type for Name")
  }

  implicit val countryReads = Reads[Country] {
    case JsString(value) => toCountry(value).map(JsSuccess(_)).getOrElse(JsError("Not a valid Country"))
    case _ => JsError("Not a valid type for Country")
  }

  implicit val yearReads = Reads[Year] {
    case JsString(value) => toYear(value).map(JsSuccess(_)).getOrElse(JsError("Not a valid Year"))
    case _ => JsError("Not a valid type for Year")
  }

  implicit val viewsReads = Reads[Views] {
    case JsNumber(value) => toViews(value).map(JsSuccess(_)).getOrElse(JsError("Not a valid Views"))
    case _ => JsError("Not a valid type for Views")
  }

  implicit val movieReads: Reads[Movie] = (
    (__ \ "title").read[Title] and
      (__ \ "director").read[Director] and
      (__ \ "year").read[Year] and
      (__ \ "views").read[Views] and
      (__ \ "country").read[Country]
    ) (Movie.apply _)

}
