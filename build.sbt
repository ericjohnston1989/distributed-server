name := "distributed-server"

version := "0.1"

scalaVersion := "2.11.7"

organization := "com.petametrics"

//excludeFilter in unmanagedSources := "Metrics.scala"

libraryDependencies ++= Seq(
  "com.typesafe.akka"           %% "akka-actor"          % "2.3.4",
  "com.typesafe.akka"           %% "akka-remote"         % "2.3.4",
  "com.typesafe.akka"           %% "akka-testkit"        % "2.3.4",
  "com.typesafe.akka"           %% "akka-cluster"        % "2.3.4",
  "com.typesafe.akka"           %% "akka-contrib"        % "2.3.4"
)
