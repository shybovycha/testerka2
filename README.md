# testerka2

This is a programmatic solution checker for the
_Matematyczne Podstawy Informatyki_ course at _Jagiellonian University_, year of 2016.

The system currently checks the solutions to only one problem - cars in a room.

The supported solution languages are:

* CommonLisp
* C++
* C#
* Java
* Kotlin
* Python 2, Python 3
* Ruby

The system consists of two artifacts - **webapp** and **worker**.

Web application is only used as an interface - to accept the solutions and put them in a queue and to display the solution check results (e.g. how successful a solution was).

The worker performs the heavy lifting - compiling, running and verifying the solutions.

The queue is implemented on database. This way it persists the data.

Worker uses Spring Scheduler to run at certain times, pick the solutions pending and process them.

## Building

Each application component requires a separate build.

### Web application

First, install the front-end dependencies by navigating to the `src/main/resources/static` and running

```
$ npm install
```

**IMPORTANT:** you will also have to specify the JDBC params to connect to the database in `src/main/resources/application.properties`. Currently the system only supports MySQL and PosgreSQL.

Then, create a JAR file with Maven and using the `worker` profile:

```
$ mvn clean package -P webapp
```

### Solution checker (worker)

Build the JAR with `worker` Maven profile:

```
$ mvn clean package -P worker
```

## Running

First of all, you will need a database on your server. Install one according to your system requirements.

Then you will need to create the database schema. Use Flyway for that _(**NOTE: THE DATA IS SAMPLE; USE YOUR OWN CREDENTIALS**)_:

```
$ mvn flyway:migrate -Dflyway.url=jdbc:postgresql://localhost:5432/testerka2 -Dflyway.user=postgres -Dflyway.password=Secret
```

Simplest way to run the application is using `java -jar`. Together with a task manager such as `pm2`,
one can easily manage both parts of the system on a server.

Bare in mind: you will have to put the `application.properties` file in the working directory or pass the Spring datasource params as `-D` properties to the application.

```
$ java -jar webapp/target/testerka2-webapp-0.1.1-SNAPSHOT-runnable.jar
$ java -jar worker/target/testerka2-worker-0.1.1-SNAPSHOT-runnable.jar
```
