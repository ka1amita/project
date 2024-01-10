# Committed project

## Try it!

### Prerequisites

+ _MySQL_ DB
+ _npm_
+ _NGINX_

### Set-up DB

+ set up MySQL DB like this

```shell
DB_URL=jdbc:mysql://localhost:3306/committed_app?serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=password
```

> [!TIP]  
> You can use different values and override them later in the set-up

### Start Backend

Starting from the _Git_ repository root:
1. `cd backend`
2. `cp .env.sample .env`
   > [!TIP]
   > You can now override the DB credentials inside `.env`
3. `./gradlew bootRun`

### Start Frontend

1. `cd ../frontend` directory (parent of this `README.md` file) if not already
2. `npm install`
3. set environmental variable corresponding to the backend address
   `export REACT_APP_API_URL="http://localhost:8080/"`
4. `npm run build`
5. `cp -r build/ /opt/homebrew/var/www/`
   > [!WARNING]  
   > Will override (default) files!
6. `cp -r build/ /opt/homebrew/var/www/`
7. `nginx -c $(pwd)/nginx/config`
8. [check it here](http://127.0.0.1:80)
9. [register](http://127.0.0.1/auth/register) and [login](http://127.0.0.1/auth/login)
   > [!NOTE]  
   > It's mostly mock template, but you can create some [todos](http://127.0.0.1:80/todos)

### CircleCI Dashboard

[![CircleCI](https://circleci.com/gh/green-fox-academy/simensis-osic-devops-zwei.svg?style=svg&circle-token=1fb56c98fa1dccdd4290292136985d0732e51e59)](https://app.circleci.com/pipelines/github/green-fox-academy/simensis-osic-devops-zwei?branch=develop)

[![CircleCI](https://dl.circleci.com/insights-snapshot/gh/green-fox-academy/simensis-osic-devops-zwei/develop/build_and_test/badge.svg?window=7d&circle-token=1fb56c98fa1dccdd4290292136985d0732e51e59)](https://app.circleci.com/insights/github/green-fox-academy/simensis-osic-devops-zwei/workflows/build_and_test/overview?branch=develop&reporting-window=last-7-days&insights-snapshot=true)
