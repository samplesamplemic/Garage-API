# Garage API

### Description

Restful API service based on three types of vehicles: car, moto and van. 
\
Framework: [Spring][Spring-url], <strong>Spring Boot</strong> - is a tool that makes developing web application and microservices with Spring Framework faster and easier .
\
Database: [h2][h2-url], the java SQL  in-memory database.
\
Documentation: [springdoc-openapi][Springdoc-url], java library helps to automate the generation of API documentation using spring boot projects.
<br></br>
Pattern used:

- DAO pattern to isolate persistence layer, developed into <i>service</i> folder, 
  from the business layer, i.e. the controllers;
- Value Object and Factory Method for some properties validation in the entity, i.e. Times, Doors and CargoCapacity classes;
- DTO, developed into <i>model</i> folder;

***

### Built With

- [![Spring][Spring.io]][Spring-url]
- [![h2][h2-db]][h2-url]
- [![springdoc][springdoc.io]][springdoc-url]

***

### Link:

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Documentation in JSON format: http://localhost:8080/v3/api-docs
- h2 database console: http://localhost:8080/console/

<!-- MARKDOWN LINKS & IMAGES -->

[Spring-url]: https://spring.io/
[Spring.io]: https://img.shields.io/badge/Spring-4A4A55?style=for-the-badge&logo=spring&logoColor=#6db33f
[h2-url]: https://www.h2database.com/html/main.html
[h2-db]: https://img.shields.io/badge/h2%20database-0000bb?style=for-the-badge&logo=h2&logoColor=#6db33f
[springdoc-url]:https://springdoc.org/
[springdoc.io]:https://img.shields.io/badge/sprindoc-4A4A55?style=for-the-badge&logo=swagger&logoColor=#6db33f