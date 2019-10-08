<div align="center">
  <h2>Notebook Server</h2>

![GitHub release](https://img.shields.io/github/release/salah3x/notebook-server.svg?color=%23f441be)
![GitHub](https://img.shields.io/github/license/salah3x/notebook-server.svg?color=%232196F3)

---

A simple notebook server that can execute pieces of code in an interpreter using Spring Boot and GraalVM.

See also [notebook-ui](https://github.com/salah3x/notebook-ui).

</div>

---

## Pre-requisite

- GraalVM must be installed in order to be able to build the project, follow the [Getting Started with GraalVM](https://www.graalvm.org/docs/getting-started/).
- You can use the Graal Updater to install language packs for Python, R, and Ruby.
  > `$ gu install python`
- Install latest version of [Maven](https://maven.apache.org/).

## Installation

- First clone the repo:
  > `$ git clone git@github.com:salah3x/notebook-server.git`
- Build the project:
  > `$ mvn package -DskipTests`
- Run the jar file:
  > `$ java -jar target/notebook-server-0.0.1-SNAPSHOT.jar`
- If all is okay, you should be able to interact with the server in [http://localhost:8080/](http://localhost:8080/)

## Usage

- The `/execute` end-point accepts JSON as request body. The json object must have the following format:
  ```
  {
    "code": "string",
    "sessionId": "string"
  }
  ```
  > See [InterpreterRequest.java](https://github.com/salah3x/notebook-server/blob/master/src/main/java/com/github/salah3x/notebookserver/controller/dtos/InterpreterRequest.java) for more information about these fields.
- The code must have the format:
  ```
  %[LANG]
  [CODE]
  ...
  ```
  > Example:
  >
  > ```
  > %js
  > 1 + 1;
  > "Hello ".concat("world!");
  > ```
- The server returns a json object as response. The response have the following format:

  ```
  {
    "success": "boolean",
    "result": "string",
    "sessionId": "string"
  }
  ```

  > See [InterpreterResponse](https://github.com/salah3x/notebook-server/blob/master/src/main/java/com/github/salah3x/notebookserver/controller/dtos/InterpreterResponse.java) for more info about these fields.

## Further help

To get started with GraalVM visite [the home page](https://www.graalvm.org/).

To get more information about spring boot and maven go to [the official guides](https://spring.io/guides/gs/spring-boot/).

---

This project was generated from [Spring Initializr](https://start.spring.io).