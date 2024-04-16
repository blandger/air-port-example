## AirPort j2ee example

The `Air port` is an example Jakarta EE application with a HTTP endpoint that is running in
[WildFly j2ee container](https://wildfly.org).

The `src/main` folder contains a Jakarta EE application using JAX-RS.

# Prerequisite installation

### Install JDK 17 and check
Use any suitable approach to install JDK 17. Then check it using command below

>java --version

The output should be something like :

```
openjdk 17.0.9 2023-10-17
OpenJDK Runtime Environment GraalVM CE 17.0.9+9.1 (build 17.0.9+9-jvmci-23.0-b22)
OpenJDK 64-Bit Server VM GraalVM CE 17.0.9+9.1 (build 17.0.9+9-jvmci-23.0-b22, mixed mode, sharing)
```

## Docker + docker compose

### Install Docker + docker compose tools into your system
See how to install it [from Docker guide/doc](https://docs.docker.com/engine/install/)

### Run and Check PostgreSQL docker-compose server

When you have Docker compose properly installed there is a prepared script in project root folder: docker-compose.yml

> sudo docker compose up

OR

> docker compose up

You can see PostgreSQL running in docker compose. 
```
............................
postgres-1  | PostgreSQL init process complete; ready for start up.
postgres-1  |
postgres-1  | 2024-04-04 10:18:24.519 UTC [1] LOG:  starting PostgreSQL 16.2 (Debian 16.2-1.pgdg120+2) on x86_64-pc-linux-gnu, compiled by gcc (Debian 12.2.0-14) 12.2.0, 64-bit
postgres-1  | 2024-04-04 10:18:24.519 UTC [1] LOG:  listening on IPv4 address "0.0.0.0", port 5432
postgres-1  | 2024-04-04 10:18:24.519 UTC [1] LOG:  listening on IPv6 address "::", port 5432
postgres-1  | 2024-04-04 10:18:24.531 UTC [1] LOG:  listening on Unix socket "/var/run/postgresql/.s.PGSQL.5432"
postgres-1  | 2024-04-04 10:18:24.545 UTC [61] LOG:  database system was shut down at 2024-04-04 10:18:24 UTC
postgres-1  | 2024-04-04 10:18:24.554 UTC [1] LOG:  database system is ready to accept connections
```

### Install PostgreSQL CLI client
Linux: Check how to install PSQL CLI client only by [link](https://www.dewanahmed.com/install-psql/)

Windows: Download 16.2 installation package, install PSQL CLI client only by [link](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)

Check after installation

>psql --version
```
psql (PostgreSQL) 14.11 (Ubuntu 14.11-0ubuntu0.22.04.1)
```

Check SQL connection using command:
> psql postgres://postgres:example@localhost:5432

Possible output should be like :
```
psql (14.11 (Ubuntu 14.11-0ubuntu0.22.04.1), server 16.2 (Debian 16.2-1.pgdg120+2))
WARNING: psql major version 14, server major version 16.
Some psql features might not work.
Type "help" for help.

postgres=#
```
### Create database (IMPORTANT !)
We'll create a new database (air_port_example) MANUALLY and new schema (air_port_schema) with all tables and initial data will be created AUTOMATICALLY on startup.

Run command in CLI to create new db schema
> postgres=# SELECT 'CREATE DATABASE air_port_example' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'air_port_example')\gexec

Sometimes more simple form can work too (not always):
> postgres=# CREATE DATABASE air_port_example;

you can show SQL schema list by command inside SQL server like:

> postgres=#\l

```
                                                          List of databases
       Name       |  Owner   | Encoding | Locale Provider |  Collate   |   Ctype    | ICU Locale | ICU Rules |   Access privileges
------------------+----------+----------+-----------------+------------+------------+------------+-----------+-----------------------
 air_port_example | postgres | UTF8     | libc            | en_US.utf8 | en_US.utf8 |            |           |
 postgres         | postgres | UTF8     | libc            | en_US.utf8 | en_US.utf8 |            |           |
 template0        | postgres | UTF8     | libc            | en_US.utf8 | en_US.utf8 |            |           | =c/postgres          +
                  |          |          |                 |            |            |            |           | postgres=CTc/postgres
```
Exit SQL client by:

>postgres=#\q

## WildFly application server

### Download and unpack WildFly application server
Download ZIP/TGZ **31.0.0.Final** version ONLY from [the download page](https://www.wildfly.org/downloads/)
or by direct link:

[https://github.com/wildfly/wildfly/releases/download/31.0.0.Final/wildfly-31.0.0.Final.zip](https://github.com/wildfly/wildfly/releases/download/31.0.0.Final/wildfly-31.0.0.Final.zip)

unpack it into local folder like : /wildfly-31.0.0.Final/

### Check WildFly can start up without errors

wildfly-31.0.0.Final/> ./bin/standalone.sh

See output like
```
.................
15:08:59,361 INFO  [org.jboss.as.server] (Controller Boot Thread) WFLYSRV0212: Resuming server
15:08:59,364 INFO  [org.jboss.as] (Controller Boot Thread) WFLYSRV0060: Http management interface listening on http://127.0.0.1:9990/management
15:08:59,364 INFO  [org.jboss.as] (Controller Boot Thread) WFLYSRV0051: Admin console listening on http://127.0.0.1:9990
15:08:59,365 INFO  [org.jboss.as] (Controller Boot Thread) WFLYSRV0025: WildFly Full 31.0.0.Final (WildFly Core 23.0.1.Final) started in 3481ms - Started 569 of 790 services (335 services are lazy, passive or on-demand) - Server configuration file in use: standalone.xml
```

## PosgreSQL driver installation into WildFly server

Driver library should be installed into running WildFly container using JBoss Admin CLI tool

### 1. Open terminal and run WildFly server by command :
> ./wildfly-31.0.0.Final/bin/standalone.sh

check it's started correctly.

### 2. Open second terminal and run Admin CLI tool by:
> ./wildfly-31.0.0.Final/bin/jboss.cli

See output from the CLI like :
```
You are disconnected at the moment. Type 'connect' to connect to the server or 'help' for the list of supported commands.
[disconnected /]
```
type and run next command inside CLI to connect to running WildFly:

> [disconnected /] connect

See no errors on connect.

### 3. Run command to install new module/driver JAR file like:

Specify correct local path to postgresql-42.7.3.jar file from project:
/air-port-example/lib/postgresql-42.7.3.jar

> [standalone@localhost:9990 /] module add --name=org.postgres --resources=path_to/air-port-example/lib/postgresql-42.7.3.jar --dependencies=javax.api,javax.transaction.api

If you put 'postgresql-42.7.3.jar' inside /wildfly-31.0.0.Final/bin 

> [standalone@localhost:9990 /] module add --name=org.postgres --resources=postgresql-42.7.3.jar --dependencies=javax.api,javax.transaction.api

Check and see no errors in CLI

Type to quit:
> [standalone@localhost:9990 /] quit

### 4. Restart (stop and start again) WildFly server

> wildfly-31.0.0.Final/bin/standalone.sh

Watch there are no errors during start up after adding new module


### Replace XML config file in WildFly app server (31.0.0.Final)
**_IMPORTANT! Backup (rename) original WildFly config file in folder :_**
/wildfly-31.0.0.Final/standalone/configuration/standalone.xml

Take updated config with additional PostgreSQL datasource from project folder :
/air-port-example/wildfly_config/standalone.xml

put it into wildfly install folder (standalone/configuration/standalone.xml), then check again running by command:

> /wildfly-31.0.0.Final/bin/standalone.sh

See output about bound **airPortExampleDS** data source:

```
16:24:29,245 INFO  [org.wildfly.extension.undertow] (MSC service thread 1-1) WFLYUT0006: Undertow HTTPS listener https listening on 127.0.0.1:8443
16:24:29,251 INFO  [org.jboss.as.ejb3] (MSC service thread 1-5) WFLYEJB0493: Jakarta Enterprise Beans subsystem suspension complete
16:24:29,281 INFO  [org.jboss.as.connector.subsystems.datasources] (MSC service thread 1-8) WFLYJCA0001: Bound data source [java:jboss/datasources/airPortExampleDS]
```

Check there is no errors like:
```
Caused by: org.postgresql.util.PSQLException: FATAL: database "air_port_example" does not exist
```

## Install MAVEN build tool and check
Use any suitable approach to install Maven. Version 3.6.4 was used. Then check it using command below

>mvn --version

The output should be something like :
```
Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
Maven home: /opt/maven
Java version: 17.0.9, vendor: GraalVM Community, runtime: /home/username/.sdkman/candidates/java/17.0.9-graalce
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "5.15.0-101-generic", arch: "amd64", family: "unix"
```

## Building the application
To build the application, you use Maven:

> mvn clean package

Maven will compile the application, provision a WildFly server
The WildFly server is created in `target/server` with the application deployed in it.

## Deploying application into running WildFly app server

You should run WildFly first by command (server is running on deploy):

>/wildfly-31.0.0.Final/bin/standalone.sh

then run sources build and deploy command

> mvn clean package wildfly:deploy

Correct WAR app deployment logs without errors looks like below: 
```
15:04:05,077 INFO  [org.wildfly.security.soteria.original.SamRegistrationInstaller] (ServerService Thread Pool -- 83) Initializing Soteria 3.0.3.Final for context '/airport-example'
15:04:05,428 INFO  [org.jboss.resteasy.resteasy_jaxrs.i18n] (ServerService Thread Pool -- 83) RESTEASY002225: Deploying jakarta.ws.rs.core.Application: class org.airport.example.AirPortApplication
15:04:05,732 INFO  [org.wildfly.extension.undertow] (ServerService Thread Pool -- 83) WFLYUT0021: Registered web context: '/airport-example' for server 'default-server'
15:04:05,784 INFO  [org.jboss.as.server] (management-handler-thread - 2) WFLYSRV0010: Deployed "airport-example.war" (runtime-name : "airport-example.war")
```

IF you see error like below, then you probably didn't run/start WildFly instance properly
```
[ERROR] Failed to execute goal org.wildfly.plugins:wildfly-maven-plugin:4.2.1.Final:deploy (default-cli) 
on project airport-example: Failed to execute goal deploy.: 
java.net.ConnectException: WFLYPRT0053: Could not connect to remote+http://localhost:9990. 
The connection failed: Connection refused -> [Help 1]
```

## Testing REST API
You can use a Postman.com account for importing Environment and Collection [placed into folder](/wildfly_config/Postman.com_test_calls_exported)

Deployed application API is accessible by URL: http://127.0.0.1:8080/airport-example

All api use - Content-Type: application/json

### User endPoints

POST
/users/register
```
{"username":"user family name FIFO","email":"test_user_01@gmail.com","password":"jwt#$%ge"}
```

POST
/users/login
```
{"email":"admin@gmail.com","password":"Dfks$5d*Q"}
```

POST
/users/logout
Header = Authorization: Bearer eyJraWQiOi......

### AirPort endpoints

POST
/airports
```
Header = Authorization: Bearer eyJraWQiOi......
{"name":"New AirPort name","code":"OIWW","city":"New City name"}
```

PUT
/airports/{id}
```
Header = Authorization: Bearer eyJraWQiOi......
{"name":"New AirPort 22","code":"YJDEW","city":"New City 22"}
```

DELETE
/airports/{id}
```
Header = Authorization: Bearer eyJraWQiOi......
```

GET
/airports

GET
/airports?name=Ne

GET
/airports?code=O

GET
/airports?name=Ne&code=O
