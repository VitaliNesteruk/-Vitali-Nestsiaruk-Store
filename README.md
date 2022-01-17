# Vitali-Nestsiaruk-Store
Store of products

## Requirements
* JDK 11
* Apache Maven

## Build application:
```
mvn clean install

## Run integration tests:
```
mvn clean verify

## Run project information ( coverage, dependency, etc. ):
```
mvn site
mvn site:stage

Open in browser: 
${project}/target/staging/index.html
