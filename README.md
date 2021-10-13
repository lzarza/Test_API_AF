# Airfrance User API Test Offer

This is the API required for the Test offer for Air France - KLM.
The aim is to register and search an user into a user database.

## Prerequisites

- Java JDK 11 minimum
- Maven for loading dependancies

## Compile

- Get source code and import as Maven Project in your IDE
- Modify application.properties to use your own database, the projects contains an embedded H2 database for test purposes.
- modify data.sql to set up you own settings. This script manage to set up the pair of country names where registration is available and the minimum age to register. (Default : France, 18)
- Build application using maven clean install

## Run and Test

- Run API using maven spring-boot:run
- You can see your springboot api is running using this link : http://localhost:8080/test-api-af/swagger-ui.html
- You may find some postman requests for test your application

## Note from developper

- Having countries and age as settings haven't been requested. But business rules always change and all restrictions shall be easily changed without redeploying the application. 
