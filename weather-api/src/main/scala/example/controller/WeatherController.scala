package example.controller

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import spray.json._
import DefaultJsonProtocol._
import scala.util.{Success, Failure}
import scala.concurrent.{ExecutionContext, Future}

import example.service.WeatherService

class WeatherController(weatherService: WeatherService)(implicit
    ec: ExecutionContext
) {

  // Define a route to get weather by city name
  val route: Route = path("weather") {
    get {
      parameter("city") { city =>
        onComplete(weatherService.getWeatherForCity(city)) {
          case Success(resp) =>
            // Check if the response contains an error
            resp.asJsObject.getFields("error") match {
              case Seq(JsString(errorMessage)) =>
                complete(
                  HttpResponse(
                    StatusCodes.BadRequest,
                    entity =
                      JsObject("error" -> JsString(errorMessage)).compactPrint
                  )
                )
              case _ =>
                complete(
                  HttpResponse(StatusCodes.OK, entity = resp.compactPrint)
                )
            }
          case Failure(ex) =>
            complete(
              HttpResponse(
                StatusCodes.InternalServerError,
                entity =
                  JsObject("error" -> JsString(ex.getMessage)).compactPrint
              )
            )
        }
      }
    }
  }
}

