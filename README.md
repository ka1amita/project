# Committed project

<!-- TOC -->
* [Committed project](#committed-project)
  * [Introduction](#introduction)
  * [Description](#description)
  * [Getting started](#getting-started)
    * [Prerequisites](#prerequisites)
    * [Set-up DB](#set-up-db)
    * [Run it](#run-it)
      * [Using IntelliJ](#using-intellij)
        * [Start Backend](#start-backend)
        * [Start Frontend](#start-frontend)
      * [Using shell](#using-shell)
        * [Start Backend](#start-backend-1)
        * [Start Frontend](#start-frontend-1)
    * [Try it](#try-it)
  * [My Contributions](#my-contributions)
  * [CircleCI Dashboard](#circleci-dashboard)
<!-- TOC -->

## Introduction

This project was created by me and three other students during the last month of a bootcamp.

The app was deployed to multiple environments on cloud but is currently offline.

## Description

Todo app. Created with _Spring_ API and _React_ frontend.

## Getting started

### Prerequisites

+ _MySQL_ DB
+ _IntelliJ_ IDEA or _npm_ and _NGINX_

### Set-up DB

+ set up MySQL DB according to the following table:

| property      | value            |
|---------------|------------------|
| address       | `localhost:3306` |
| database name | `committed_app`  |
| user          | `root`           |
| password      | `password`       |

> [!TIP]  
> You can use different values and override them later in the set-up

### Run it

#### Using IntelliJ

##### Start Backend

1. Open project (`File > Open..`) form the `backend` directory
2. `cp .env.sample .env`
> [!TIP]
> You can now override the DB credentials inside `.env`
3. Using the _IntelliJ_'s _Run Configurations_ run the default configuration (or run
   the `com/gfa/ProjectApplication.java` file)

##### Start Frontend

1. Open project (`File > Open..`) form the `frontend` directory
2. `cp .env.sample .env`
3. Using the _IntelliJ_'s _Run Configurations_ run the `npm start` configuration
> [!NOTE]
> You might need to create it first. Don't forgot to specify the `package.json` filepath
> (you can find it in the `frontend` directory)
4. [Try it](#try-it)

#### Using shell

##### Start Backend

Starting from the _Git_ repository root:
1. `cd backend`
2. `cp .env.sample .env`
> [!TIP]
> You can now override the DB credentials inside `.env`
3. `./gradlew bootRun`

##### Start Frontend

Starting from the _Git_ repository root:

1. `cd frontend` directory (parent of this `README.md` file) if not already
2. `npm install`
3. set environmental variable corresponding to the backend address
   `export REACT_APP_API_URL="http://localhost:8080/"`
4. `npm run build`
5. `cp -r build/ /opt/homebrew/var/www/`
> [!WARNING]  
> Will override (default) files!
6. `cp -r build/ /opt/homebrew/var/www/`
7. `nginx -c $(pwd)/nginx/config`

### Try it

1. [check it here](http://127.0.0.1:80)
2. [register](http://localhost/auth/register) and [login](http://localhost/auth/login)
> [!NOTE]  
> It's mostly mock template, but you can create some [todos](http://localhost/todos)

## My Contributions

+ [x] Spring Security with JWT
+ [x] DB migrations with Flyway
+ [x] i18n and l10n
+ [x] testing with Junit and Postman
+ [x] CI/CD pipeline with CircleCI
+ [x] deployment to AWS as jar or Docker image
+ [X] [documentation](docs/)


## CircleCI Dashboard

[![CircleCI](https://circleci.com/gh/green-fox-academy/simensis-osic-devops-zwei.svg?style=svg&circle-token=1fb56c98fa1dccdd4290292136985d0732e51e59)](https://app.circleci.com/pipelines/github/green-fox-academy/simensis-osic-devops-zwei?branch=develop)

[![CircleCI](https://dl.circleci.com/insights-snapshot/gh/green-fox-academy/simensis-osic-devops-zwei/develop/build_and_test/badge.svg?window=7d&circle-token=1fb56c98fa1dccdd4290292136985d0732e51e59)](https://app.circleci.com/insights/github/green-fox-academy/simensis-osic-devops-zwei/workflows/build_and_test/overview?branch=develop&reporting-window=last-7-days&insights-snapshot=true)
