# PaymentMS
Payment Microservice for Utopia Airlines
## Requirements & Quick Start
##### -Maven
##### -MySQL
`$ mvn spring-boot:run` - run PaymentMS as a spring boot application. The application will run by default on port `8081`.

Configure the port by changing the `server.port` value in the `application.properties` file which is located in the resources folder.

The application can be run with a local MySQL database. Configure the `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` in the `application.properties` file according to your needs.
## API
`/payments` - GET : Get a list of all the payments from the DB.

`/payments/{id}` - GET : Get payment by id.

`/payments` - POST : Create a payment by providing a correct request body

`/payments` - PUT : Update a payment by providing a correct request body including the id

`/payments/{id}` - DELETE : Delete a payment by id.
