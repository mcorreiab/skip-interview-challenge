# Skip Interview Challenge
[![Spring Boot](https://img.shields.io/badge/spring--boot-3.0.5-green?style=flat-square&logo=spring-boot)](https://spring.io/)
[![Java](https://img.shields.io/badge/java-17-blue?style=flat-square&logo=oracle)](https://www.java.com/en/)
![Build and tests](https://github.com/mcorreiab/skip-interview-challenge/actions/workflows/buid-test.yml/badge.svg)

# Table of contents
- [Overview](#overview)
- [How to run the project](#how-to-run-the-project)
  - [Docker](#docker)
  - [Locally](#locally)
  - [Tests](#tests)
- [Architecture](#architecture)
  - [Events](#events)
  - [Database](#database)
- [Business rules decisions](#business-rules-decisions)
  - [Endpoint that return delivery transactions by period and courier](#endpoint-that-return-delivery-transactions-by-period-and-courier)
  - [Endpoint that returns the weekly statement of a courier](#endpoint-that-returns-the-weekly-statement-of-a-courier)

## Overview
This repository provides an implementation for SkipTheDishes interview challenge. It consists on a service that listen to events related to courier deliveries and exposes endpoints to get condensed data by period and week.

## How to run the project
### Docker
The project can be run using Docker Compose. It will start the service, a RabbitMQ instance and a PostgreSQL database. To run the project, execute the following command:
```bash
docker-compose up
```
The service will be available on port 8080. The RabbitMQ management interface will be available on port 15672. The PostgreSQL database will be available on port 5432.
To access the RabbitMQ management interface, use the following credentials:
- Username: admin
- Password: admin

To stop the project, execute the following command:
```bash
docker-compose down
```

### Locally
The service can also be run locally, but it requires a RabbitMQ instance and a PostgreSQL database. The connection details can be configured in the `application.properties` file.

To run the service locally, execute the following command:
```bash
./mvnw spring-boot:run
```

## Tests
The database for the integration tests was created using [TestContainers](https://www.testcontainers.org/). So, to run all the tests, you need to have Docker running.
The tests can be run using the following command:
```bash
./mvnw test
```

## Architecture

The service uses RabbitMQ to listen to the three events, with each event being posted to a different queue. The data from each event is saved in a relational database

![image](https://user-images.githubusercontent.com/37126173/232259882-da10161b-b17d-4e84-844c-04412619d7b7.png)

### Events
The constraints for each event are:
- DeliveryCreated
  - **deliveryId** - Shouldn't be blank
  - **courierId** - Shouldn't be blank
  - **createdTimestamp** - Shouldn't be null
  - **value** - Shouldn't be a negative number
- AdjustmentModified
  - **adjustmentId** - Shouldn't be blank
  - **deliveryId** - Shouldn't be blank
  - **modifiedTimestamp** - Shouldn't be null
  - **value** - Shouldn't be null
- BonusModified
  - **bonusId** - Shouldn't be blank
  - **deliveryId** - Shouldn't be blank
  - **modifiedTimestamp** - Shouldn't be null
  - **value** - Shouldn't be a negative number
  
If an event fails to meet these restrictions, it's rejected and not requeued. However, if an `AdjustmentModified` or `BonusModified` arrive before the `DeliveryCreated` event, it will be requeued to wait for the delivery event to be received and created first.

### Database
The database used is PostgreSQL. A relational database was chosen to leverage the ACID properties when dealing with money, in order to ensure that any inconsistencies will not corrupt the data. The database consists of two tables:
- **deliveries**: Contains the same data that was sent by the `DeliveryCreated` event, with the `deliveryId` received serving as the primary key
- **corrections**: Both adjustments and bonuses are grouped on the same table, differentiated by a type `field` that can have a value of either be `ADJUSTMENT` or `BONUS`. It's linked to the `deliveries` table by the foreign key `delivery_id`
  - ID: It's the primary key, that is composed of both the `eventId` and the `type` concateneted. The `type` is added to the `ID` to handle the possibility of two `AdjustmentModified` and `BonusModified` events having the same `id`.
  - correctionId: Have the value of `adjustmentId` or `bonusId`

## Business rules decisions
### Endpoint that return delivery transactions by period and courier
For this endpoint, was considered both the period and the courier to be mandatory. The idea is to provide an endpoint for the courier to see his deliveries for a given period.

The endpoint searches for all the deliveries in the period and adds the value of all the adjustments and bonuses to the delivery value. The result is then grouped by week and courier, and the sum of the values is returned. Since the goal is to provide a condensed view of the deliveries, the date of the adjustments and bonuses is not considered.

The response returned by the endpoint is paginated, as is expected a huge volume of data for an active courier and its possible to search for a longer period.

The content is
```json
[
  {
    "deliveryId": "Id of the delivery",
    "courierId": "Courier linked to the delivery",
    "date": "Creation date of the delivery",
    "amount": 5.50 // Sum of the delivery value and the adjustments and bonuses
  }
]
```
### Endpoint that returns the weekly statement of a courier
This endpoints receives the date of the week and the courier id. It returns the sum of the deliveries, adjustments and bonuses for the given week.

Since this endpoint is a statement, the date of the adjustments and bonuses is considered. The endpoint searches for all the deliveries in the period and adds the value of all the adjustments and bonuses to the delivery value.

It uses all the data that it finds for the week of the reference date, even if the delivery was created in a previous week. The idea is to provide a statement of the courier for the given week, not only the deliveries that were created in that week.

```json
{
  "courierId": "Courier linked to the deliveries",
  "weekStart": "Start date of the week",
  "weekEnd": "End date of the week",
  "totalAmount": 5.50 // Sum of the delivery value and the adjustments and bonuses
}
```