# Charging session backbone

This is a result of a EVBox backend case. I do not know if assignment should be timeboxed but I started working on the case at 14:15 on April 21st and finished the case at 17:31 (I was trying to produce a solution in under 4 hours).

## Software stack

  - Spring Boot 2.1.3
  - Java 8
  - Built with maven 3

## Build and run
Before building and running the app, you need to have Java 8 installed on your machine (maven is also a pre).

Full build with tests:
> mvn clean install

Build without tests:
> mvn clean install -DskipTests

You can run application in multiple ways - by using mvn plugin:
> mvn spring-boot:run

or directly with Java:
> java -jar target/charging-session-backbone-0.0.1-SNAPSHOT.jar

All endpoints specified in a case are exposed on the root of 8080 port.


## Decisions during development

After reading a case, I initially wanted to develop it using Vert.x since I really like the Vert.x approach to concurrency and testing concurrent calls, but decided on using Spring since, in production environment, I would as well decide on using widely-spread Spring Boot.

The reason why I used ConcurrentHashMap as in-memory data storage is:
- It is thread-safe without having to sync entire map implementation,
- Reads are non-blocking (and fast), writes are done with a lock ensuring data integrity.

In a production environment, concurrency issue would be tackled on a persistent/database level.
