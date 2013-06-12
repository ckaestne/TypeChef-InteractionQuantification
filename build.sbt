name := "TypeChef Interaction Quantification Scripts"

version := "0.0.1"

scalaVersion := "2.10.1"

libraryDependencies += "de.fosd.typechef" % "frontend_2.10" % "0.3.4"

libraryDependencies += "junit" % "junit" % "4.8.2" % "test"

libraryDependencies += "com.novocode" % "junit-interface" % "0.6" % "test"

libraryDependencies +=   "org.scalatest" % "scalatest_2.10.0" % "1.8" % "test" 


TaskKey[File]("mkrun") <<= (baseDirectory, fullClasspath in Runtime, mainClass in Runtime) map { (base, cp, main) =>
  val template = """#!/bin/sh
java -ea -Xmx2G -Xms128m -Xss10m -classpath "%s" %s "$@"
"""
  val mainStr = ""
  val contents = template.format(cp.files.absString, mainStr)
  val out = base / "run.sh"
  IO.write(out, contents)
  out.setExecutable(true)
  out
}
