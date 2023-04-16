# Skip Interview Challenge
[![Spring Boot](https://img.shields.io/badge/spring--boot-3.0.5-green?style=flat-square&logo=spring-boot)](https://spring.io/)

This repository provides an implementation for SkipTheDishes interview challenge. It consists on a service that listen to events related to courier deliveries and exposes endpoints to get condensed data by period and week.

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
