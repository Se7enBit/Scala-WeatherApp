package example

import akka.actor.ActorSystem
import akka.Done
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.actor.CoordinatedShutdown
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import concurrent.ExecutionContext.Implicits.global

import example.service.WeatherService
import example.controller.WeatherController
import example.controller.Routes

object Main {

  def main(args: Array[String]): Unit = {
    // Define host and port as variables
    val host = "localhost"
    val port = 9000

    // Initialize ActorSystem
    given system: ActorSystem = ActorSystem("weather-api-system")

    // Instantiate WeatherService and WeatherController
    val weatherService = new WeatherService()
    val weatherController = new WeatherController(weatherService)

    // Bind server Routes
    val bindingFuture: Future[ServerBinding] =
      Http().newServerAt(host, port).bind(Routes.allRoutes(weatherController))

    bindingFuture.onComplete {
      case scala.util.Success(binding) =>
        println(s"Server online at http://$host:$port/")
        println(s"Health check available at http://$host:$port/heartbeat")

        // Shut down on SIGTERM or SIGINT
        CoordinatedShutdown(system).addTask(
          CoordinatedShutdown.PhaseServiceUnbind,
          "unbind-http"
        ) { () =>
          binding.unbind().map { _ =>
            println("Server unbound successfully.")
            Done
          }
        }

      case scala.util.Failure(ex) =>
        println(s"Server could not start: ${ex.getMessage}")
        system.terminate()
    }

    Await.result(system.whenTerminated, Duration.Inf)
  }
}

