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

## Docker Usage

- The Dockerfile is split into two images, one for building the Native Image and
another to run that Native Image in a lean, efficient `Alpine` Linux container with
limited permissions for extra security
- Recommended Docker commands:
  - `docker build -t <name>/infection_protection_native_backend -f Dockerfile.builder`
    - Run as needed to make new builds of the Spring Boot backend Native Image
  - `docker build -t <name>/infection_protection_backend_host -f Dockerfile.runner`
    - Pulls the Native Image created by the `Dockerfile.builder` and hosts it
  - `docker run --name <name> -d --env-file <env-file-path> -p <ip:port:port> --rm <name>/infection_protection_backend_host`
    - Runs the Native Image in the host image with a name for the container, the
    local env file loaded, and exposed on the provided port
    - `--rm` flag ensures the container is removed after it finishes/stops running
- Other helpful commands:
  - `docker ps` - Lists out containers
  - `docker images`
  - `docker stop <container-name>`
  - `docker rmi <image-name>` - Remove images by their name

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
