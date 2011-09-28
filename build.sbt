name := "nurupo"

organization := "org.riedelcastro.nurupo"

// The := method used in Name and Version is one of two fundamental methods.
// The other method is <<=
// All other initialization methods are implemented in terms of these.
version := "0.1-SNAPSHOT"

scalaVersion := "2.9.1"

// Add multiple dependencies
libraryDependencies ++= Seq(
     "junit" % "junit" % "4.8" % "test",
     "log4j" % "log4j" % "1.2.16"
)
