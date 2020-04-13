package controllers

import javax.inject.Inject
import java.util.UUID
import play.api.mvc.{BaseController, ControllerComponents}
import play.api.libs.json.Json

class PetsController @Inject()(
  val controllerComponents: ControllerComponents
) extends BaseController {

  val store = collection.mutable.Map.empty[String, models.Pet]

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

  val missingContentType = UnprocessableEntity("Expected 'Content-Type' set to 'application/json'")
  val missingPetForm = UnprocessableEntity("Expected content to contain a pet form")

  def post = Action { req =>
    req.body.asJson
      .toRight(missingContentType)
      .flatMap(_.asOpt[models.PetForm].toRight(missingPetForm))
      .map { form =>
        val id = UUID.randomUUID().toString
        val model = models.Pet(id, form.name, form.tag)

        store.update(id, model)
        val json = Json.toJson(model)
        Created(json)
      }
      .merge
  }

  def putById(id: String) = Action { req =>
    req.body.asJson
      .toRight(missingContentType)
      .flatMap(_.asOpt[models.PetForm].toRight(missingPetForm))
      .flatMap { form =>
        store.get(id)
          .toRight(NotFound(s"Pet not found: ${id}"))
          .map((_, form))
      }
      .map { case (found, form) =>
        val model = models.Pet(found.id, form.name, form.tag)
        store.update(found.id, model)

        NoContent
      }
      .merge
  }

  def deleteById(id: String) = Action {
    store.get(id)
      .fold(NotFound(s"Pet not found: ${id}")) { _ =>
        store.remove(id)

        NoContent
      }
  }
}
