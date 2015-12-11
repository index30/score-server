name := """score-server"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "com.typesafe.play" %% "anorm" % "2.4.0",
  evolutions,
  "org.webjars" % "jquery" % "2.1.3",
  "com.h2database" % "h2" % "1.4.190" // replace `${H2_VERSION}` with an actual version number
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

// Compile the project before generating Eclipse files,
//so that generated .scala or .class files for views and routes are present
EclipseKeys.preTasks := Seq(compile in Compile)
