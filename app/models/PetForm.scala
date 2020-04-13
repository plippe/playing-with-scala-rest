package models

import play.api.libs.json.Json

case class PetForm(
  name: String,
  tag: Option[String],
)

object PetForm {
  implicit def petFormPlayJsonReads = Json.reads[PetForm]
}
