# GLOW Restaurant Service

The GLOW Restaurant Service is a backend microservice responsible for restaurants and their incoming orders within the GLOW platform. It is independently deployable and forms part of the platform's distributed microservice architecture.

Each restaurant has a name, phone number, address and weekly opening hours. When a customer checks out, the order service sends the order to the restaurant through this service, and restaurant staff then update its status as they prepare it. Every status change is also sent back over REST to the order service, which is where customers follow the progress of their order.

The service is built with **Java 25** and **Quarkus**, using **PostgreSQL** for persistence and **Liquibase** for database migrations. Restaurants are stored in the database, while incoming orders are currently held in memory and are cleared when the service restarts.

Technologies:
* Java 25 & Quarkus
* PostgreSQL & Liquibase
* REST API & REST Client
* OpenID Connect (OIDC)
* MapStruct
* Kubernetes
* Gradle
