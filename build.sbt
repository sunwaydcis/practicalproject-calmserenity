ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "introtosclafx",
    libraryDependencies ++= {
      // Determine OS version of JavaFX binaries
      val osName = System.getProperty("os.name") match {
        case n if n.startsWith("Linux")   => "linux"
        case n if n.startsWith("Mac")     =>
          val arch = System.getProperty("os.arch")
          if (arch == "aarch64" || arch == "arm64") "mac-aarch64" else "mac"
        case n if n.startsWith("Windows") => "win"
        case _                            => throw new Exception("Unknown platform!")
      }
      Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
        .map(m => "org.openjfx" % s"javafx-$m" % "21.0.4" classifier osName)
    },
    libraryDependencies ++= Seq("org.scalafx" %% "scalafx" % "21.0.0-R32"),

    // --- ADDED DEPENDENCIES FOR DATABASE ---
    libraryDependencies ++= Seq(
      "org.scalikejdbc"   %% "scalikejdbc"      % "4.2.0",
      "org.apache.derby"  %  "derby"            % "10.14.2.0",
      "org.slf4j"         %  "slf4j-nop"        % "2.0.9"
    )
  )
