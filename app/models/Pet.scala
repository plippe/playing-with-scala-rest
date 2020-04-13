package models

import play.api.libs.json.Json

case class Pet(
  id: String,
  name: String,
  tag: Option[String],
)

object Pet {
  implicit def petPlayJsonWrites = Json.writes[Pet]
}
