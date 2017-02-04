CECZ4024
=========

## Setup

This is a standard eclipse project. Choose `File` / `Import` / `Existing Projects into Workspace` and there should be no errors.
The recommended eclipse version is >=4.4.

```
├── .classpath
├── .gitignore
├── .project
├── lib  # library files; DON'T add dependencies
│   ├── bcprov-jdk15on-156.jar     # crypto
│   ├── logback-classic-1.1.9.jar  # logging
│   ├── logback-core-1.1.9.jar     # logging
│   └── slf4j-api-1.7.5.jar        # logging
├── readme.md                      # this file
├── run.py                         # automation script run before submission
└── src
    ├── common
    │   ├── Config.java            # Don't touch/import
    │   └── Utils.java             # helper functions
    ├── logback.xml                # logging configuration
    ├── p1
    │   ├── Attacker.java          # implement & submit
    │   └── Oracle.java            # oracle; don't touch
    └── p2
        ├── Attacker.java          # implement & submit
        └── Oracle.java            # oracle; don't touch
```

## Submission

The submission files include Java source files and the PDF report files inside one folder, with the structure like below.
KEEP DIRECTORY STRUCTURE and DO NOT SUBMIT ANY OTHER FILES.
The whole folder should then be compressed as a zip file. The zip file name should be `CECZ4024_${Matriculation}.zip`, where `${Matriculation}` is your matriculation number; e.g., if your number is `U123`, then the file name is `CECZ4024_U123.zip`.

```
├── p1
│   ├── Attacker.java      # copied from src/p1/Attacker.java
│   └── Report.pdf
└── p2
    ├── Attacker.java      # copied from src/p2/Attacker.java
    └── Report.pdf
```

We also provide a script `run.py` to automate the submission zip file creation procedure. Before running that, make sure there is a `pdf` folder inside project root directory, which has the structure:

```
pdf
├── p1
│   └── Report.pdf
└── p2
    └── Report.pdf
```