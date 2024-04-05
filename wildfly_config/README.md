# PosgreSQL driver installation into WildFly

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

### 3. Run command to install driver JAR file like:

Specify correct local path to postgresql-42.7.3.jar file from project:
/air-port-example/lib/postgresql-42.7.3.jar

> [standalone@localhost:9990 /] module add --name=org.postgres --resources=path_to/air-port-example/lib/postgresql-42.7.3.jar --dependencies=javax.api,javax.transaction.api

See no errors in CLI

# Update WildFly configuration (one time)

### Replace XML config file in WildFly app server (31.0.0.Final)
**_IMPORTANT! Backup original WildFly config file in folder :_**
/wildfly-31.0.0.Final/standalone/configuration/standalone.xml

Take updated config with additional PostgreSQL datasource from project folder :
/air-port-example/wildfly_config/standalone.xml

put it into wildfly install folder, then check again running by command:

> /wildfly-31.0.0.Final/bin/standalone.sh

See output about bound **airPortExampleDS** data source:

```
16:24:29,245 INFO  [org.wildfly.extension.undertow] (MSC service thread 1-1) WFLYUT0006: Undertow HTTPS listener https listening on 127.0.0.1:8443
16:24:29,251 INFO  [org.jboss.as.ejb3] (MSC service thread 1-5) WFLYEJB0493: Jakarta Enterprise Beans subsystem suspension complete
16:24:29,281 INFO  [org.jboss.as.connector.subsystems.datasources] (MSC service thread 1-8) WFLYJCA0001: Bound data source [java:jboss/datasources/airPortExampleDS]
```

