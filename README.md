# BuildTools [![Build Status](https://travis-ci.org/TorchPowered/BuildTools.svg?branch=master)](https://travis-ci.org/TorchPowered/BuildTools)
A tool application to building Torch stable builds easily and effectively.

## Building
BuildTools can be built manually using Gradle to recieve a JAR file.

### Prerequisites
- Java 7 or above

### Cloning
Please clone this project via this command to make sure the project is cloned properly.
```git clone --recursive https://github.com/TorchPowered/BuildTools.git```

### Gradle
To build the project, do either these steps depending on your platform on the directory that the repository is cloned to.
**Windows:**  ```gradlew assemble```
**Linux and Mac OS X:**  ```./gradlew assemble```
There will be a build folder where the libs folder will contain the JAR file from the build.

### Issues
If you're building the project and running the JAR file from the build and get a SecurityException,
try this:
1. Open the JAR file with a archive manager
2. Remove everything from META-INF except MANIFEST.MF

That is because of shading from Gradle to input all the dependencies inside one JAR.