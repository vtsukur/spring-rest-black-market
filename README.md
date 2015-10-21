# spring-rest-black-market

![Build status](https://travis-ci.org/vtsukur/spring-rest-black-market.svg?branch=master)

## Building and Running

Make sure that [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) is installed and is on the path.

Project uses [Gradle](http://gradle.org/) as a build tool. Building is as easy as running the following shell command on Linux / Unix systems:

    ./gradlew build

This is the respective shell command to run on Windows:

    gradlew build

Once the application is built, run it as follows:

    java -jar build/libs/spring-rest-black-market.jar

Navigate to [http://localhost:8080](http://localhost:8080) and surf through the black market ;)

## Development

### IntelliJ IDEA

Recommended version of the IDE is 14+

Just import `build.gradle` file from the IDE itself using *File -> Open ...*

Make sure that annotation processors are enabled and received from the project classpath.
When using IntelliJ IDEA 14 this feature is activated in
*Preferences* screen under *Build, Execution, Deployment -> Compiler -> Annotation Processors*
path where *Enable annotation processing* must be checked and
*Obtain processors from project classpath* option must be selected.
This is utterly important for the project to compile.

Boot up the server by locating `Application` class
and running it as *Application* or *Spring Boot*.

#### Static Content

All static content put under `src/main/resources/static` will be exposed automatically
(like any other application based on Spring Boot).
To hot deploy static sources just trigger compilation in the IDE and refresh the page in the browser.
To get all client side dependencies just run [npm](https://www.npmjs.com)

    npm install

#### AdBlock issue

If you have the errors like ```GET http://localhost:8080/ads/search net::ERR_BLOCKED_BY_CLIENT``` please add this url
into AdBlock exception (white list). This happens because AdBlock automatically blocks url that contains the word *ads*