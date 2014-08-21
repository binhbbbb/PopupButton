import org.vaadin.sbt.VaadinPlugin._
import sbt.Keys._
import sbt.ScalaVersion

name := "PopupButton"

version in ThisBuild := "2.4-SNAPSHOT"

organization in ThisBuild := "org.vaadin.hene"

crossPaths in ThisBuild := false

autoScalaLibrary in ThisBuild := false

javacOptions in (ThisBuild, Compile) ++= Seq("-source", "1.6")

javacOptions in (ThisBuild, compile) ++= Seq("-target", "1.6")

lazy val root = project.in(file(".")).aggregate(addon, demo)

lazy val addon = project.settings(vaadinAddOnSettings :_*).settings(
  name := "PopupButton",
  libraryDependencies := Dependencies.addonDeps,
  // Javadoc generation causes problems so disabling it for now
  mappings in packageVaadinDirectoryZip <<= (packageBin in Compile, packageSrc in Compile) map {
    (bin, src) => Seq((bin, bin.name), (src, src.name))
  },
  sources in doc in Compile := List()
)

lazy val demo = project.settings(vaadinWebSettings :_*).settings(
  name := "popupbutton-demo",
  artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) => "PopupButton." + artifact.extension },
  libraryDependencies := Dependencies.demoDeps,
  javaOptions in compileVaadinWidgetsets := Seq("-Xss8M", "-Xmx512M", "-XX:MaxPermSize=512M"),
  vaadinOptions in compileVaadinWidgetsets := Seq("-strict", "-draftCompile"),
  enableCompileVaadinWidgetsets in resourceGenerators := false,
  javaOptions in vaadinDevMode ++= Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"),
  // JavaDoc generation causes problems
  sources in doc in Compile := List()
).dependsOn(addon)
