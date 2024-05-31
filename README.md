# testerka2

This is a solution checker (concept similar to LeetCode, HackerRank and many others) for the
_Matematyczne Podstawy Informatyki_ course at _Jagiellonian University_, back in 2016.

The system currently checks the solutions to only one problem, "cars in a room".

Supported languages for the solutions are:

* CommonLisp
* C++
* C#
* Java
* Kotlin
* Python 2, Python 3
* Ruby

The system consists of two artifacts - **webapp** and **worker**.

Web application is a web interface used to upload the solutions and store them in a queue and to display the solution check results (e.g. solution correctness or how many test cases does the solution cover).

The worker does the heavy lifting - compiling, running and verifying the solutions.

The queue is implemented in a database. This is a naive persistent queue implementation.

Worker uses Spring Scheduler to process the queue at a given time intervals by picking the unprocessed solutions, compiling, running them and calculating the points.

## Building

Application comes with Maven wrapper, which builds an entire application as its submodules.
Building is as simple as running

```shell
./mvnw clean package
```

**IMPORTANT:** you will need to specify the database connection params (JDBC URI) in the `application.properties` file.

Currently, the system only supports MySQL and PosgreSQL.

## Running

Create the database schema using FlywayDB on the `db` project:

```shell
$ ./mvnw clean flyway:migrate -pl db -Dflyway.configFiles=src/main/resources/application.properties
```

or

```shell
$ ./mvnw flyway:migrate -pl db -Dflyway.url=jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME -Dflyway.user=$DB_USER -Dflyway.password=$DB_PASSWORD
```

Simplest way to run the application is using `java -jar`. Coupled with a task manager such as `pm2`,
one can easily manage both parts of the system on a server.

Bear in mind: you will need to put the `application.properties` file in the working directory (next to the JAR file) or pass the Spring datasource params via `-D` command line arguments to the application:

```shell
$ java -jar webapp/target/testerka2-webapp-0.1.1-SNAPSHOT-runnable.jar
$ java -jar worker/target/testerka2-worker-0.1.1-SNAPSHOT-runnable.jar
```

or

```shell
$ java -jar webapp/target/testerka2-webapp-0.1.1-SNAPSHOT-runnable.jar \
  -Dspring.datasource.url=jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME \
  -Dspring.datasource.username=$DB_USER \
  -Dspring.datasource.password=$DB_PASSWORD
```
