import android.Keys._

android.Plugin.androidBuild

scalaVersion := "2.11.1"

platformTarget in Android := "android-19"

name := "fiteye-glass"

libraryDependencies += "com.google.zxing" % "android-core" % "3.1.0"

libraryDependencies += "com.google.zxing" % "core" % "3.1.0"
