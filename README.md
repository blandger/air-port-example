## AirPort j2ee example

The `Air port` is an example Jakarta EE application with a HTTP endpoint that is running in
[WildFly j2ee container](https://wildfly.org).

The `src/main` folder contains a Jakarta EE application using JAX-RS.

# Prerequisite installation

### Install Docker + docker compose tools to your system
See how to install it [from Docker guide/doc](https://docs.docker.com/engine/install/)

### Run and Check PostgreSQL docker-compose server

When you have Docker compose properly installed there is a prepared script in project folder: docker-compose.yml

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

### Optionally you can install PostgreSQL CLI client
Check how to do that by [link](https://www.dewanahmed.com/install-psql/)

Check after installation

>psql --version
```
psql (PostgreSQL) 14.11 (Ubuntu 14.11-0ubuntu0.22.04.1)
```

Check SQL connection using command:
psql -l

>psql postgres://postgres:example@localhost:5432

Possible output can be like :
```
psql (14.11 (Ubuntu 14.11-0ubuntu0.22.04.1), server 16.2 (Debian 16.2-1.pgdg120+2))
WARNING: psql major version 14, server major version 16.
Some psql features might not work.
Type "help" for help.

postgres=#
```

you can show SQL schema list by command on SQL server like:

>>postgres=#\dn+

```
                                       List of schemas
Name  |       Owner       |           Access privileges            |      Description       
--------+-------------------+----------------------------------------+------------------------
public | pg_database_owner | pg_database_owner=UC/pg_database_owner+| standard public schema
|                   | =U/pg_database_owner                   |
(1 row)
```

Exit SQL client by:

>postgres=#\q

### Install JDK 17 and check
Use any suitable approach to install JDK 17. Then check it using command below

>java --version

The output should be something like :

```
openjdk 17.0.9 2023-10-17
OpenJDK Runtime Environment GraalVM CE 17.0.9+9.1 (build 17.0.9+9-jvmci-23.0-b22)
OpenJDK 64-Bit Server VM GraalVM CE 17.0.9+9.1 (build 17.0.9+9-jvmci-23.0-b22, mixed mode, sharing)
```

### Install MAVEN build tool and check
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

### Download and Install WildFly app server (31.0.0.Final) 
Download 31.0.0.Final version from the [WildFly download page](https://www.wildfly.org/downloads/)

Unpack it into folder and check running by command:

> /wildfly-31.0.0.Final/bin/standalone.sh

OR

> /wildfly-31.0.0.Final/bin/standalone.bat


## Building the application
To build the application, you use Maven:

> mvn clean package

Maven will compile the application, provision a WildFly server
The WildFly server is created in `target/server` with the application deployed in it.

## Deploying application into running WildFly app server

You should run WildFly first by command:

>/wildfly-31.0.0.Final/bin/standalone.sh

then run sources build and deploy command

> mvn clean package wildfly:deploy

IF you see error like below, then you probaly didn't run WildFly instance properly

```
[ERROR] Failed to execute goal org.wildfly.plugins:wildfly-maven-plugin:4.2.1.Final:deploy (default-cli) 
on project airport-example: Failed to execute goal deploy.: 
java.net.ConnectException: WFLYPRT0053: Could not connect to remote+http://localhost:9990. 
The connection failed: Connection refused -> [Help 1]
```


