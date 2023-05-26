# Infection Prevention - Spring Boot Backend
  - The Spring Boot backend for the Infection Prevention mobile apps, providing data storage and authentication for the 
  AngularJS SPA front-end as well as the iOS and Android apps
  
## Features
  - Currently, data is stored in a MongoDb instance hosted on MongoDb Atlas BUT there are several alternatives
    - PostgreSQL is the natural choice since it integrates well with most deployment options as well as Hibernate
    - MariaDB is a free open source version of MySQL that works with Hibernate but it seems the only option is AWS RDS
      - Since it is based on MySQL, it's worth noting that errors can slip through the cracks if not careful. PostgreSQL
      would likely reject many of those errors rather than allow the save to occur anyway
    - Apache Cassandra is a good NoSQL DB that could replace MongoDb with an extremely generous hosting option on Datastax 
    through AstraDB BUT it might not be nearly as performant 
      - It has a table-like structure under the hood, so it actually exists somewhere between an SQL column/table database and a MongoDb document one 
      - It seems to provide a zero downtime, majorly reliable and easy to horizontally scale database BUT struggles with data relationships.
      - Spring Data Cassandra + Astra Spring Boot help but overcoming the data relationship issue requires careful data duplication or denormalizing
  - Currently, data is accessed via RESTful repositories BUT Spring Boot makes using GraphQL very easy so the two options 
  may coexist giving me the opportunity to judge GraphQL from a mobile app rather than a Javascript frontend
    - HealthPractice and Precaution now include each other's full data without causing endlessly recursive JSON responses
  - Enable OAuth2 authentication as a faster, more secure login method that conveniently suits SPAs and mobile apps.
  - SwaggerUI added to document API endpoints, with help from SpringDoc OpenAPI, which enables Spring Boot 3 support

### Future Considerations
  - The Employee data model likely needs a belongsTo relationship to account for linking supervisors to their employees BUT how to handle it?
    - Is another data model needed?
    - Should there be a "isSupervisor" property stored?
    - Or a computed property where the presence of other employees pointing to a particular employee's ID determines if someone is the supervisor
      - This would likely require a 2nd property that specifies an Employee's supervisor which results in a recursive relationship 
      where each Employee instance has a property referencing their supervisor and one referencing their employees
        - Spring can actually make this easier! @DocumentReference can be used on List property in combination with @ReadOnlyProperty
        which means MongoDB won't save a list of IDs, only the supervisor ID. From there it can use the supervisor ID to find every
        employee of the supervisor when we want a List<Employee>. While a potential performance hit, it DOES save space in the DB
        and it could even make using Apache Cassandra more feasible. [See this blog](https://spring.io/blog/2021/11/29/spring-data-mongodb-relation-modelling)
  - Configure the JacksonMapper's Serializer to simplify cases where One-to-Many relationships can cause endless recursion
    - Current solution happens at the Controller level, iterating over lists to remove each backReference before Jackson causes
    the recursion. It's more of an issue for GetAll endpoints rather than getById endpoints.
  - Email notifications sent to supervisors if employee is reported
  - MongoDB can handle simple images fairly well but 5gb data limitation may be pretty significant compared to AWS or Cassandra

## Related Apps
- Android App: https://github.com/NLCaceres/Android-Records
    - Nearing feature parity again (still needs several views missing in iOS)
    - Targets Android 13 Tiramisu (Sdk 33) to 8 Oreo (Sdk 26)
    - Future Developments
        - Begin using Jetpack Compose to create views
        - Add Room Library for local caching
- Front-end website: https://github.com/NLCaceres/Ang-records
    - Running Angular 12 with major redesign coming soon
    - Deploy to Vercel since CORS settings wouldn't need changing anyway?
- iOS App: https://github.com/NLCaceres/iOS-records 
    - In the process of better separating code for readability and reusability by extracting business logic out 
    of ViewModels and into Repositories/Services + Domain-layer reusable functions 
    - Will need to add search bar to ReportList view for filtering
    - Will need to feed data into SwiftUI Charts