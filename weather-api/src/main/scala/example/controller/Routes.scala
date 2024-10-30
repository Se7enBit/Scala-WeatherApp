package example.controller

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.`Access-Control-Allow-Origin`

import example.controller.WeatherController

object Routes {
  val corsHeaders = respondWithHeader(`Access-Control-Allow-Origin`("http://localhost:5173"))

  // Define routes
  val rootRoute: Route = pathEndOrSingleSlash {
    get {
      complete("Welcome to ScalaWeatherApp!")
    }
  }

  val heartbeatRoute: Route = path("heartbeat") {
    get {
      complete("OK")
    }
  }

  // Combine routes with WeatherController
  def allRoutes(weatherController: WeatherController): Route = corsHeaders {
    concat(
      rootRoute,
      heartbeatRoute,
      weatherController.route
    )
  }
}

