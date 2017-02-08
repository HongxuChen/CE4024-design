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
├── readme.html                    # readme
├── readme.md                      # readme (markdown version)
├── run.py                         # automation script run before submission
└── src
    ├── Attacker1.java          # implement & submit
    ├── Attacker2.java          # implement & submit
    ├── Oracle1.java            # oracle; don't touch/import
    ├── Oracle2.java            # oracle; Don't touch/import
    ├── common
    │   ├── Config.java         # Don't touch/import
    │   └── Utils.java          # Don't touch/import
    └── logback.xml             # logging configuration
```

## Tips

- You can use helper functions in `Utils.java`
- You might also import utility packages inside `lib/`
- DON'T expose your implementations to publically available sites, e.g., GitHub, BitBucket.

## Submission

The submission files include Java source files and the PDF report files inside one folder, with the structure like below.  
KEEP DIRECTORY STRUCTURE and DO NOT SUBMIT ANY OTHER FILES.

The whole folder should then be compressed as a zip file. The zip file name should be `${Matriculation}_${YOUR_NAME}.zip`, where `${Matriculation}` is your matriculation number, `${YOUR_NAME}` is the name with ' '(whitespace) substituted to '_'(underscore); e.g., if your number is `U1234567` and your name is `James Ong`, then the file name is `U1234567_James_Ong.zip`.

```
├── Attacker1.java    # copied from src/Attacker1.java
├── Attacker1.pdf
├── Attacker2.java    # copied from src/Attacker2.java
└── Attacker2.pdf
```

We also provide a script `run.py` to automate the submission zip file creation procedure. Before running that, make sure there is a `pdf` folder inside project's root directory, which has the structure:

```
pdf
├── Attacker1.pdf
└── Attacker2.pdf
```