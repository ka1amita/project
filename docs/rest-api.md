# REST API Documentation

<!-- TOC -->
* [REST API Documentation](#rest-api-documentation)
  * [About this document](#about-this-document)
    * [Audience](#audience)
    * [Scope](#scope)
    * [Prerequisites](#prerequisites)
  * [Authentication and Authorization](#authentication-and-authorization)
    * [JWT Implementation](#jwt-implementation)
    * [JWT Configuration](#jwt-configuration)
    * [JWT Example](#jwt-example)
      * [Access JWT](#access-jwt)
      * [Refresh JWT](#refresh-jwt)
    * [Password persistence](#password-persistence)
    * [Roles](#roles)
    * [Authentication and authorization workflow](#authentication-and-authorization-workflow)
  * [Endpoint](#endpoint)
    * [Summary](#summary)
    * [OpenAPI specification](#openapi-specification)
<!-- TOC -->

## About this document

### Audience

This document is intended for a software developers.

### Scope

This document focuses on the following aspects of the Committed application:

+ type of authentication and authorization implemented
+ summary of the endpoints

This document doesn't describe any of the following:

+ frontend implementation of the above
+ implementations of the above on the code level

### Prerequisites

This document requires familiarity with the following topics:
+ Spring Boot Security
+ REST API
+ JWT

## Authentication and Authorization

### JWT Implementation

The Committed application authenticates and authorizes users by a **JSON Web Token** (**JWT**). The
JWT is implemented with [Java JWT] 4.4.0. The authorization is stateless.

Two types of JWT carrying the following payload are implemented:

| Token type  | Payload                                        |
|-------------|------------------------------------------------|
| Access JWT  | username<br>roles<br>expiration date<br>issuer |
| Refresh JWT | username<br>expiration date<br>issuer          |

The JWT authentication and authorization is realised inside following two filters and a single
config:

| Class name                   | Extends                                | Overridden methods                                                                    |
|------------------------------|----------------------------------------|---------------------------------------------------------------------------------------|
| `CustomAuthenticationFilter` | `UsernamePasswordAuthenticationFilter` | `attemptAuthentication`<br>`successfulAuthentication`<br>`unsuccessfulAuthentication` |
| `CustomAuthorizationFilter`  | `OncePerRequestFilter`                 | `doFilterInternal`                                                                    |
| `SecurityConfig`             | -                                      | `filterChain`                                                                         |

The following specific ways how JWTs are received and send are implemented:

+ The JWTs are received inside a request Authorization header with a `Bearer ` prefix.
+ The JWTs are send in JSON format inside a response body.

Access JWT can be refreshed by using the refresh JWT. The refresh is implemented
in `TokenController`. Access JWT is refreshed by sending the refresh JWT by a POST method
at `/token/refresh` endpoint.

### JWT Configuration

Some JTW properties are hardcoded according the following table:

| Property          | Value   |
|-------------------|---------|
| Signing algorithm | HMAC256 |
| Roles claim key   | "roles" |

Other JWT properties can be configured using Gradle properties or environmental variables according
to the following table:

| Property                 | Gradle property            | Environmental variable |
|--------------------------|----------------------------|------------------------|
| Access token expiration  | `token.expiration.access`  | `JWT_ACCESS_EXP`       |
| Refresh token expiration | `token.expiration.refresh` | `JWT_REFRESH_EXP`      |
| Secret                   | `token.secret`             | `JWT_SECRET_KEY`       |

[Java JWT]: https://mvnrepository.com/artifact/com.auth0/java-jwt

### JWT Example

#### Access JWT

Header:

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

Payload:

```json
{
  "sub": "Matej Kala",
  "exp": 1705731876,
  "iss": "https://www.committed.com/login",
  "roles": [
    "ADMIN"
  ]
}
```

#### Refresh JWT

Header:

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

Payload:

```json
{
  "sub": "Matej Kala",
  "exp": 1705731876,
  "iss": "https://www.committed.com/login"
}
```
### Password persistence

Passwords are stored hashed using the `BCryptPasswordEncoder`.

### Roles

The following four roles are defined in the Committed application:

+ Root
+ Admin
+ User
+ Guest

The Committed application allows for an optional root user with the Root role. The root user can be
configured using Gradle properties or environmental variables according to the following table:

| Property         | Gradle property                            | Environmental variable |
|------------------|--------------------------------------------|------------------------|
| Enable root user | `spring.flyway.placeholders.root.enable`   | `ROOT_ENABLE`          |
| Root's username  | `spring.flyway.placeholders.root.username` | `ROOT_USERNAME`        |
| Root's password  | `spring.flyway.placeholders.root.password` | `ROOT_PASSWORD`        |
| Root's email     | `spring.flyway.placeholders.root.email`    | `ROOT_EMAIL`           |
| Root's role      | `spring.flyway.placeholders.root.role`     | `ROOT_ROLE`            |

### Authentication and authorization workflow

Successful authentication and authorization follows this workflow:

1. User submits credentials (email and password) using a login form to frontend.
2. The frontend sends the credentials to the backend.
3. The backend authenticates the credentials against credentials stored in database.
4. The access JWT - refresh JWT pair is generated and send back to the frontend.
5. Frontend sends the access JWT with every subsequent request.
6. The backend validated the access JWT and authorizes the request.

After expiration of the access JWT, these additional steps are followed:

1. The frontend sends the refresh JWT.
2. The backend validated the refresh JWT.
3. New access JWT - refresh JWT pair is generated and send back to the frontend.

## Endpoint

### Summary

Several endpoints are available. The following tables states which of them bypass the
authentication in the `CustomAuthorizationFilter` and whether authorization is required for them.

| Endpoint                    | Authenticated | HTTP Methods not requiring authorization |
|-----------------------------|---------------|------------------------------------------|
| `/reset/`                   | No            | POST                                     |
| `/reset/{resetCode}`        | No            | POST                                     |
| `/api/todos/`               | Yes           | -                                        |
| `/api/todos/{id}`           | Yes           | -                                        |
| `/confirm/{activationCode}` | No            | GET                                      |
| `/register`                 | No            | POST                                     |
| `/token/refresh`            | No            | POST                                     |
| `/api/users/`               | Yes           | -                                        |
| `/api/users/deleted`        | Yes           | -                                        |
| `/api/users/{id}`           | Yes           | -                                        |
| `/dashboard/`               | Yes           | -                                        |
| `/strings`                  | No            | all                                      |
| `/ribbon`                   | No            | GET                                      |

### OpenAPI specification

Open [OpenAPI specification](committed-openapi.yaml) draft is attached.
