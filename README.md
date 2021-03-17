# AirportMS
Airport Microservice for Utopia Airlines
## Requirements & Quick Start
##### -Maven
##### -MySQL
`$ mvn spring-boot:run` - run AirportMS as a spring boot application. The application will run by default on port `8082`.

Configure the port by changing the `server.port` value in the `application.properties` file which is located in the resources folder.

The application can be run with a local MySQL database. Configure the `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` in the `application.properties` file according to your needs.
## API
`/airports` - GET : Get a list of all the airports from the DB.

`/airport/{id}` - GET : Get an airport by id.

`/airports` - POST : Create an airport by providing a correct request body

`/airports` - PUT : Update an airport by providing a correct request body including the id

`/airports/{id}` - DELETE : Delete an airport by id.

