# access-request-api

This API is developed as part of a pilot project for a national application platform for applying access to restricted
data or expressing interest in unpublished data. The goal is to simplify and standardize the process for both data
providers and users, ensuring that valuable data can be shared in a secure and controlled manner.

Users can submit requests directly via data.norge.no. The solution also allows users to register interest in data that
is not yet ready for sharing. This provides data providers with insights into demand and a better basis for assessing
whether they should prioritize making these data available.

## Getting started

Clone the repository.

### Prerequisites

* Java 21
* Maven

### Running locally

Run the application in your favorite IDE with the **dev** profile activated (see ```resources/application.yaml```).

Visit ```http://localhost:8080/swagger-ui.html``` for the OpenAPI specification.

### Formatting code

This project uses [ktlint](https://github.com/gantsign/ktlint-maven-plugin) to enforce a consistent code style.
To automatically fix formatting violations, run:

```sh
mvn ktlint:format
```
