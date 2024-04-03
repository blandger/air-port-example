## AirPort j2ee example

The `Air port` is an example Jakarta EE application with a HTTP endpoint that is running in
[WildFly j2ee container](https://wildfly.org).

The `src/main` folder contains a Jakarta EE application using JAX-RS.

## Prerequisite installation

### Install Docker + docker compose tools to your system
See how to install it [from guid](https://docs.docker.com/engine/install/)

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

