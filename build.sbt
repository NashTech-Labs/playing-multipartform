name := """playing-multipartform"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(jdbc, anorm, cache, ws)

libraryDependencies ++= Seq(
  	"org.webjars" 			%% 	"webjars-play" 		% "2.3.0-2",
  	"org.webjars" 			%	"bootstrap" 		% "3.1.1-2",
  	"org.webjars" 			% 	"bootswatch-darkly" % "3.3.1+2",
  	"org.webjars" 			% 	"html5shiv" 		% "3.7.0",
  	"org.webjars" 			% 	"respond" 			% "1.4.2"
)

ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;views.*;"

ScoverageSbtPlugin.ScoverageKeys.coverageMinimum := 80

ScoverageSbtPlugin.ScoverageKeys.coverageFailOnMinimum := true

ScoverageSbtPlugin.ScoverageKeys.coverageHighlighting := {
	if (scalaBinaryVersion.value == "2.10") false
	else false
}

publishArtifact in Test := false

parallelExecution in Test := false

