# Garage API

### Description

Rest API service based on three types of vehicles: car, moto and van. I initially followed this
tutorial: [Building REST services with Spring][tutorial-rest-spring-url]. Implementation of Hateoas library
to facilitate REST representations with self-ref hypermedia links.
Three main branches of links to communicate with the application by JSON format, i.e. localhost:
port/garage/${type-vehicle} (type-vehicle: cars - moto - van).
I used a plugin, called <i>[git-commit-id-maven-plugin][git-commit-id-maven-url]</i>, with own configuration to request
git local cached info
at localhost:port/actuator/info(for the plugin to work properly you need to start the project with this command: <i>mvn
spring-boot:run</i>).
\
**Framework**: [Spring][Spring-url], <strong>Spring Boot</strong> - is a tool that makes developing web application and
microservices with Spring Framework faster and easier.
\
**Database**: [h2][h2-url], the java SQL in-memory database.
\
**Testing**: The testing area need to be improved. Integration test || Unit Test: <i>[JUnit-5][junit-url]</i>
\
**Documentation**: [springdoc-openapi][Springdoc-url], java library helps to automate the generation of API
documentation
using spring boot projects.
<br></br>
Pattern used:

- DAO pattern to isolate persistence layer, developed into <i>service</i> folder,
  from the business layer, i.e. the <i>controllers</i>;
- Value Object and Factory Method for some properties' validation, located in the <i>entity</i> folder, like Times,
  Doors and
  CargoCapacity
  classes;
- DTO to carry data between process, developed into <i>model</i> folder;
- Error handling: the exception throws an advice(built with @ControllerAdvice strategy,
  reference: [error handling][error-handling-url]) for the unacceptable request.

***

### Built With

- [![Spring][Spring.io]][Spring-url]
- [![h2][h2-db]][h2-url]
- [![springdoc][springdoc.io]][springdoc-url]

***

### Link:

- port: 8080
- Swagger UI: http://localhost:port/swagger-ui/index.html
- Documentation in JSON format: http://localhost:port/v3/api-docs
- h2 database console: http://localhost:port/console/
- git info: http://localhost:port/actuator/info

### Testing:

- Integration test: [Mockito][mockito-url]. I divided into two section: business layer/controller tests and
  persistence/service layer tests;
- Unit test: [JUnit-5][junit-url]

<!-- MARKDOWN LINKS & IMAGES -->

[Spring-url]: https://spring.io/

[Spring.io]: https://img.shields.io/badge/Spring-4A4A55?style=for-the-badge&logo=spring&logoColor=#6db33f

[h2-url]: https://www.h2database.com/html/main.html

[h2-db]: https://img.shields.io/badge/h2%20database-0000bb?style=for-the-badge&logo=h2&logoColor=#6db33f

[springdoc-url]:https://springdoc.org/

[springdoc.io]:https://img.shields.io/badge/sprindoc-4A4A55?style=for-the-badge&logo=swagger&logoColor=#6db33f

[git-commit-id-maven-url]:https://github.com/git-commit-id/git-commit-id-maven-plugin

[junit-url]: https://junit.org/junit5/

[error-handling-url]: https://www.baeldung.com/exception-handling-for-rest-with-spring

[tutorial-rest-spring-url]: https://spring.io/guides/tutorials/rest/

[mockito-url]: https://site.mockito.org/