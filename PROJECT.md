# Fox Dining Project

- [Fox Dining Project](#fox-dining-project)
  - [Overview](#overview)
  - [Backend](#backend)
    - [Flyway](#flyway)
  - [REST Testing](#rest-testing)
  - [Libraries](#libraries)
  - [Frontend](#frontend)

## Overview

For this project you will build a (simple) restaurant management and billing system.

Before focusing on the restaurant specific code you will write a (reusable) Java library for user management featuring users, roles, permissions and teams - `com.gfa.users`. You will then use the library to implement staffing a loyalty membership. The `users` library will include some advanced features such as 2FA, email verification and password reset/recovery.

Similarly, before focusing on restaurant reservations and billing, you will create a (reusable) set of Java libraries for generic bookings and billings and then use those libraries in the restaurant specific code - `com.gfa.billing`, `com.gfa.bookings`

The restuarant management will feature the following:
- staff (user management); users in the 'hr' role/team/permission will be able to create, edit and delete staff (users) via [REST API](README.md#useful-links)
- users in the manager role/team/permission will be able to create menus (breakfast, lunch, dinner, party, bday, new years, etc), create/edit/delete menu items (cheese platter, sirloin steak, cheesecake, etc) and menu item categories (appetizer, meal, drink, dessert, etc)
- users in the waiter role/team/permission will be able to create a new (table) check/bill, add billable (menu) items, pay/split the check/bill, apply discounts/vouchers
- users in the manager/waiter (staff) role/team/perm will be able to create table reservations (bookings)

Some data can/should be added to the application through the db migrations (to be available at the start):
- tables (2, 4, 6, vip, room/saloon)
- permissions ("manage menus", "manage staff", "wait the tables", "manage tags", etc.)
- roles, teams, ...

Time permitting you will build basic [React](https://reactjs.org/) frontend for the restaurant manager, for the waiting staff, and for customers.

## Backend

A basic SpringBoot project is already set up for you in the [backend](backend/) folder. The `build.gradle` includes most dependencies you'll need, but some are commented out. You'll also need to other sections to the `build.gradle` file to have dependencies such as `flyway` working correctly

### Flyway

Use the [Flyway](https://flywaydb.org/) package to create database migrations for the project. Place all migrations to the `src/main/resources/db/migration/` folder and configure flyway in `application.properties`, i.e.

```bash
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.out-of-order=true
```

## REST Testing

If you need a refresher on REST testing in Java have a look at this [walkthrough](docs/walkthrough-rest-test.md)

## Libraries

Most of the classes you'll add to the reusable libraries will be/should be abstract. 

Endpoints defined in the library controllers will be relative to the subclass `@RequestMapping`. The abstract (superclass) constructor should use dependency injection to get the dependencies. That was the default implementation exponsing mappings to GET, POST, PATCH, DELETE (etc.) can be reused by multiple REST controlers. 

Let's have a quick look at a very simple example, in which we have an abstract UserController superclass, and two subclasses:
- StaffController
- MemberController

`UserController`
```java
package com.gfa.users.controllers;

import com.gfa.common.dtos.ResponseDto;
import com.gfa.users.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public abstract class UserController {

  private final UserService service;

  public UserController(UserService service){
    this.service = service;
  }

  @GetMapping("/")
  public ResponseEntity<ResponseDto> index(){
    return service.index();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseDto> show(@PathVariable Long id){
    return service.show(id);
  }
}
```

`StaffController`
```java
package com.gfa.foxdining.controllers;

import com.gfa.users.controllers.UserController;

import com.gfa.whitelabeltest.services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff")
public class StaffController extends UserController {

  @Autowired
  public StaffController(StaffService service) {
    super(service);
  }
}
```

`MemberController`
```java
package com.gfa.whitelabeltest.controllers;

import com.gfa.whitelabeltest.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController extends com.gfa.users.controllers.UserController {

  @Autowired
  public MemberController(MemberService service) {
    super(service);
  }
}
```

This implementation together with the `UserService` interface

```java
package com.gfa.users.services;

import com.gfa.common.dtos.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
  ResponseEntity<ResponseDto> index();
  ResponseEntity<ResponseDto> show(Long id);
}
```

enables use to use the endpoints and code defined in `UserController` but implement the logic in the `StaffService` and `MemberService` classes within the `foxdining` package (while reusing the `users` library code). 

That means that the following endpoints will be defined (exposed) with the code above:
```
/staff
/staff/{id}
/members
/members/{id}
```

In a similar manner we can have a very basic `UserService` implementation (to index, create, update, destroy users) done in let's say `com.gfa.users.DatabaseUserService`, into which we inject the repository dependency (the same way we injected the service dependency into the controllers above). `DatabaseUserService` can be extended by `StaffService` and `MemberService` and the single library service will then be used to manage both the `staff` and `members` users (two completely independent user bases).

## Frontend

Time permitting you will build basic [React](https://reactjs.org/) frontend for the restaurant manager, for the waiting staff, and for customers.

Have a look at the [linked](MATERIALS.md#frontend) React/frontend materials.

You should focus on functionality rather than look and feel even for the frontend application. For example, you can use the default [Bootstrap](https://getbootstrap.com/docs/5.0/getting-started/introduction/) styles and compomenents for all front end elements.

To speed up the frontend development use the [JSON Server](https://www.npmjs.com/package/json-server) package which will allow you to work with mocked data for all API request. Connect the frontend to the real backend you've developed only once the frontend is (mostly) finished