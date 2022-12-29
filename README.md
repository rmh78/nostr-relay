# nostr-relay

> **Note:** The project is still in early development and not ready for production usage.

## (1) Intro

[**nostr**](https://github.com/nostr-protocol/nostr) is a open protocol that is able to create a censorship-resistant global "social" network which consists of **nostr clients** and **nostr relays**.

This project builds a **nostr relay** using **Java** as programming language and [**Quarkus**](https://quarkus.io) as Microservices framework. With Quarkus and GraalVM native image compiler the Java program can be compiled into a native Linux binary (fast startup and minimal memory consumption).

The following Quarkus extensions are used:

| Extension                           | Documentation                                                                                         |
| ----------------------------------- | ----------------------------------------------------------------------------------------------------- |
| Quarkus Websockets                  | <https://quarkus.io/guides/websockets>                                                                |
| Quarkus Resteasy Reactive Jackson   | <https://quarkus.io/guides/rest-json> <br> <https://github.com/FasterXML/jackson>                     |
| Quarkus Hibernate ORM Panache       | <https://quarkus.io/guides/hibernate-orm-panache> <br> <https://hibernate.org/orm/documentation/5.6/> |
| Quarkus Smallrye Reactive Messaging | <https://smallrye.io/smallrye-reactive-messaging>                                                     |

## (2) Development

1. Use [SDKMAN](https://sdkman.io) to install needed software:

   - Java 17
   - Maven 3.8.7
   - Quarkus CLI 2.15.0

2. Start Quarkus dev server by running the command: `quarkus dev`

## (3) Build and run a Quarkus native image

```sh
# use the cli to compile native binary and build docker image
quarkus image build docker --native

# run nostr relay
docker run -i --rm -p 8080:8080 rmh78/nostr-relay:latest
```
