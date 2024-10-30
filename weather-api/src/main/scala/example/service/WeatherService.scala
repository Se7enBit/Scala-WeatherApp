package example.service

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import spray.json._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Properties

class WeatherService()(implicit system: ActorSystem, ec: ExecutionContext) {
  // Base URL for the API
  private val baseUrl: String = "http://api.openweathermap.org"

  // API Key from environment variable or a default value
  private val API_KEY: String =
    Properties.envOrElse("WEATHER_API_KEY", "8b84e8fd72772f6047aae17b51afde8a")

  // Use SLF4J logging with a specified source
  private val logger = Logging(system, getClass.getName)

  // Fetches weather data from the API for a list of cities matching the query
  def getWeatherList(city: String): Future[HttpResponse] = {
    val url =
      s"$baseUrl/data/2.5/find?q=$city&appid=$API_KEY&units=metric&type=like"
    Http().singleRequest(HttpRequest(uri = url))
  }

  // Processes the weather response for a given city
  def getWeatherForCity(city: String): Future[JsValue] = {
    getWeatherList(city).flatMap { response =>
      Unmarshal(response.entity).to[String].map { body =>
        try {
          // Parse the JSON response body
          val json = body.parseJson
          json // Return the parsed JSON
        } catch {
          case ex: Exception =>
            logger.error(s"Error parsing JSON response: ${ex.getMessage}")
            JsObject(
              "error" -> JsString(s"Failed to parse response: ${ex.getMessage}")
            )
        }
      }
    }
  }
}

