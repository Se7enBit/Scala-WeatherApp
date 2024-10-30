ThisBuild / scalaVersion     := "3.5.2"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

val akkaHttpVersion = "10.6.3"
val akkaVersion = "2.9.3"

// Define the root project
lazy val root = (project in file("."))
  .settings(
    name := "weather-api",
    
    // Resolvers for additional repositories
    resolvers += "Akka library repository".at("https://repo.akka.io/maven"),

    // Project dependencies
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion, 
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "io.spray" %% "spray-json" % "1.3.6", 
      "org.scalatest" %% "scalatest" % "3.2.19" % Test
    )
  )

