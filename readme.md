CECZ4024
=========

## Setup

This Java project is managed by [sbt](http://www.scala-sbt.org/index.html). The structure of the project is as follows:

```
├── build.sbt  # sbt build file
├── driver.py  # python script run before submission
├── sbt # bash wrapper for "sbt", for Linux/MacOSX users
├── sbt-launch.jar  # "sbt" jar, shouldn't be used directly
├── sbt.bat  # bat wrapper for "sbt", for Windows users
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── common
│   │   │   │   ├── Config.java
│   │   │   │   └── Utils.java
│   │   │   ├── p1
│   │   │   │   ├── Attacker.java  <== modify & submit
│   │   │   │   └── Oracle.java
│   │   │   └── p2
│   │   │       ├── Attacker.java  <== modify & submit
│   │   │       └── Oracle.java
│   │   └── resources
│   │       └── logback.xml # logging configurations
│   └── test
│       ├── resources
│       │   └── p2cases.txt        <== modify & submit
│       └── scala
│           ├── common
│           │   └── CryptoSpec.scala
│           ├── p1
│           │   └── ECBAttackerSpec.scala # sample test spec file
│           └── p2
│               └── MACAttackerSpec.scala # sample test spec file
└── submit.md
```

Prior knowledge for sbt/scala is not required. All the students need to do is to modify the 2 Java files, `src/main/java/p1/Attacker.java` and `src/main/java/p2/Attacker.java`.

To set up run `./sbt test:compile` or `sbt.bat test:compile`; the first run would take a couple of minutes since it will downloads the package dependencies.

The recommended IDEs are:

- [Eclipse4](https://eclipse.org/). You can choose `Import` / `Existing Projects into Workspace`. If that doesn't work, run `./sbt eclipse` (or `sbt.bat eclipse`) in the command line before hand.

- [Intellij](https://www.jetbrains.com/idea/#chooseYourEdition). You should select the Scala developing environment, which includes the two plugins: `sbt` and `scala`; otherwise you can install from `Preferences` / `Plugins`. Afterwards, use `File`/`New`/`Projects from Existing Sources...`, choose this project folder and select `sbt` as the build tool. See more details on https://www.jetbrains.com/help/idea/2016.3/creating-and-running-your-scala-application.html.

## Submission

The submission files the Java source files and the PDF report; for Problem 2, there is an additional file `p2cases.txt` (originally inside `src/test/resources/p2cases.txt`) containing the your test input. The whole folder should be compressed as a zip file. KEEP DIRECTORY STRUCTURE and DO NOT SUBMIT ANY OTHER FILES.

```
├── p1
│   ├── Attacker.java
│   └── Attacker.pdf
└── p2
    ├── Attacker.java
    ├── Attacker.pdf
    └── p2cases.txt  
```

Alternatively, you can run `./driver.py` to assure that you have done right before submission.
