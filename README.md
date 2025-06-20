# Infection Prevention - Spring Boot Backend

- The Spring Boot backend for the Infection Prevention mobile apps, providing data
storage and authentication for the Angular SPA frontend plus the iOS & Android apps

## Features

- Currently, data is stored in a MongoDb instance hosted on MongoDb Atlas BUT there
are alternatives like the NoSQL Apache Cassandra or AWS DynamoDB BUT the most obvious,
simple and cheapest choice is still probably PostgreSQL
- Currently, data is accessed via RESTful repositories BUT Spring Boot makes using
GraphQL very easy, giving me time to judge BOTH, especially from mobile app clients
  - HealthPractice and Precaution now include each other's full data w/out an endlessly
  recursive JSON response
  - JSON structure handled at the Model level via Jackson `@JsonView`
  - Endpoints are case-insensitive, so "/healthpractices" returns the same response
  as "/healthPractices"
- SwaggerUI documents API endpoints via SpringDoc OpenAPI for Spring Boot 3 support
- Use Mongock ChangeUnit Migrations to setup Mongo database and collections

### Future Considerations

- Enable OAuth2 as a faster, more convenient & secure login method for mobile & SPAs
- The Employee data model needs to store a bit more info like `department` & `supervisor`
  - Is another data model needed? Or would a `isSupervisor` property suffice?
  - A potentially overall simpler solution may be adding `supervisor` and `staff`
  properties. Thanks to Spring's `@DocumentReference` and `@ReadOnlyProperty` annotation,
  the `supervisor` can be saved as an ObjectID to the Employee collection documents
  while the `List<Employee> staff` will use both annotations to NOT be saved, instead
  opting to fetch `staff` using `supervisor` ID as needed. [For annotation info](https://spring.io/blog/2021/11/29/spring-data-mongodb-relation-modelling)
- Email notifications sent to supervisors if employee is reported
- Add indexes for uniqueness in Locations, HealthPractices & Precautions Mongo Collections

## Gradle Usage

- Since this project uses Gradle, it's helpful to know how to translate [Maven commands](https://docs.gradle.org/current/userguide/migrating_from_maven.html)
as well as how to update Gradle itself, `./gradlew wrapper --gradle-version <version-num>`
- Gradle Commands can be run via `./gradlew` followed by the task you want to run
  - Ex: `./gradlew clean` to clean the build folder
  - Ex: `./gradlew assemble` builds the project without testing
  - Ex: `./gradlew build` builds AND tests the project (by running `assemble` & `check`)
  - [See here for more common Base Gradle tasks](https://docs.gradle.org/current/userguide/base_plugin.html)
  - Spring-Boot also has a few helpful tasks, namely `bootJar` and `bootRun` for
  building and running your app, and these hook into the Base Gradle Plugin Tasks
  like `assemble` & `check`, ensuring every concern is covered for a Spring app
    - `bootJar` creates a runnable `.jar` file in `build/libs` as well as a unrunnable
    `plain-jar` file that contains only your project's classes and resources
    - `bootRun` simply runs the project `public static void main` in the class
    marked `@SpringBootApplication`

## Docker Usage

- The Dockerfile is split into two images, one for building the Native Image and
another to run that Native Image in a lean, efficient `Alpine` Linux container with
limited permissions for extra security
- Recommended Docker commands:
  - `docker build -t <name>/infection_protection_native_backend
                  -f Dockerfile.builder .`
    - Run as needed to make new builds of the Spring Boot backend Native Image
  - `docker build -t <name>/infection_protection_backend_host
                  -f Dockerfile.runner .`
    - Pulls the Native Image created by the `Dockerfile.builder` and hosts it
  - `docker run --name <name> -d --env-file <env-file-path>
               -p <ip:port:port> --rm <name>/infection_protection_backend_host`
    - Runs the Native Image in the host image with a name for the container, the
    local env file loaded, and exposed on the provided port
    - `--rm` flag ensures the container is removed after it finishes/stops running
- Other helpful commands:
  - `docker ps` - Lists out containers
  - `docker images`
  - `docker stop <container-name>`
  - `docker rmi <image-name>` - Remove images by their name & version/tag
- Recommended Compose commands
  - `docker compose up -d`
    - Runs `compose.yaml` in detached mode with a MongoDB instance available
    - Add `-f docker-compose.yaml` to run `docker-compose.yaml` instead of
    `compose.yaml`, starting the app, MongoDB, AND adds a Mongo-Express instance
      - Run `docker compose -f compose-mongo-express.yaml up -d` to add a Mongo
      Express instance if already running the typical `docker compose up -d`
    - Add `--env-file .env.mongo-express` to run `compose-mongo-express.yaml`
    with its override file `compose-mongo-express.override.yaml` and starts
    a Mongo instance, WITHOUT running the main app
    - Add `-f compose-jar.yaml` to run `.jar` version of the app alongside
    a MongoDB instance
  - `docker compose ls` - Lists running containers
  - `docker compose down` - Stops AND removes the running containers
    - `docker compose stop` - Stops any running containers
    - `docker compose rm` - Removes all stopped container upon confirmation
    - Adding the `--rmi` flag removes the associated container images
    - Adding the `-v` flag removes any associated volumes
- For normal Jar file based deployment, consider `eclipse-temurin`, `sapmachine`
and `ubuntu/jre`, using their JDK to build the jar file, and then a JRE to run the
App with its embedded Tomcat servlet
  - For help, check out [Dockerizing Spring Boot](https://www.baeldung.com/dockerizing-spring-boot-application)
  and [Intro to Docker Secrets](https://www.baeldung.com/ops/docker-secrets)
  in addition to [secrets in Compose](https://docs.docker.com/compose/how-tos/use-secrets/)

## Related Apps

- [Android App Github](https://github.com/NLCaceres/Android-Records)
  - Nearing feature parity again (still needs several views missing in iOS)
  - Targets Android 14 Tiramisu (SDK 34) to 8 Oreo (SDK 26)
  - Screens using more Jetpack Compose BUT XML resources are still the base
  UNTIL the Navigation Component becomes fully interoperable OR Composable
  conversion is complete, which partly depends on several common composables
  that are currently experimental becoming stable
  - Future Developments
    - Add Room Library for local caching
- [Frontend Github](https://github.com/NLCaceres/Ang-records)
  - Running Angular 18 with major redesign coming
  - Deploy to Vercel since CORS settings probably would work fine
- [iOS App Github](https://github.com/NLCaceres/iOS-records)
  - Better separating code for readability & reusability by extracting business
  logic from ViewModels and into Repositories/Services + Domain-layer reusable functions
  - Added Search Bar for Report Lists
  - Future Developments
    - Feed data into SwiftUI Charts
    - Add more filters to Report Lists
