package controllers

import javax.inject.Inject
import play.api.mvc.{BaseController, ControllerComponents}
import play.api.libs.json.Json

class PetsController @Inject()(
  val controllerComponents: ControllerComponents
) extends BaseController {

  val store = Map.empty[String, models.Pet]

  def get = Action {
    val models = store.values
    val json = Json.toJson(models)

    Ok(json)
  }

  def getById(id: String) = Action {
    store.get(id)
      .map(Json.toJson[models.Pet])
      .fold(NotFound(s"Pet not found: ${id}"))(Ok(_))
  }

}
