# Stock-Exchange-Service

## Intro

The application allow authorized users to create new stocks, update stocks prices and add stocks to already defined stockExchange.

Also, authorized users are allowed to delete stocks.

## Build

Please run the following command to build application and run test cases

```./gradlew clean build```

## Run

Please run the following command to run application

```./gradlew clean bootRun```

* Please note while starting the application, initial db script will be run automatically to create three tables:
    * `stock`

        | Column | Type |
        | :---: | :---: |
        | id | bigint |
        | name | varchar |
        | description | varchar |
        | current_price | double |
    * `stock_exchange`

      | Column | Type |
      | :---: | :---: |
      | id | bigint |
      | name | varchar |
      | description | varchar |
    * `stock_exchange_mapping`

      | Column | Type |
      | :---: | :---: |
      | stock_id | bigint |
      | stock_exchange_id | bigint |
    * `users`

      | Column |  Type   |
      |:-------:| :---: |
      | id | bigint  |
      | email | varchar |
      | password | varchar |

    * `roles`

      | Column |  Type  |
      |:------:| :---: |
      | id | bigint |
      | name | varchar |

    * `users_roles`

      | Column |  Type  |
      |:------:| :---: |
      | users_id | bigint |
      | roles | varchar |

* Also, ```stock_exchange``` table will be initialized with two stockExchanges ( "Nasdaq", "NYSE" )
* OpenAPI docs can be accessed from [swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)



## REST API

The REST API to stock-exchange-service app is described below.

## Authentication APIs

### Add new user

#### Request

`POST /signup`

    curl -X 'POST' \
            'http://localhost:8080/signup' \
            -H 'Content-Type: application/json' \
            -d '{
                "email": "user@email.com",
                "password": "123456"
            }'

#### Response

    Status: 200 Ok

### Login

#### Request

`POST /login`

    curl -X 'POST' \
            'http://localhost:8080/login' \
            -H 'Content-Type: application/json' \
            -d '{
                "email": "user@email.com",
                "password": "123456"
            }'

#### Response

    Status: 200 Ok
    Content-Type: application/json
    {
        "accessToken": "<access_token>",
        "refreshToken": "<refresh_token>"
    }

## Stock APIs


### Add new Stock

#### Request

`POST /v1/api/stock`

    curl -X 'POST' \
            'http://localhost:8080/v1/api/stock' \
            -H 'Content-Type: application/json' \
            -H 'Authorization: "Bearer <access_token>"' \
            -d '{
                "name": "WMT",
                "description": "Walmart",
                "currentPrice": 121.7
            }'

#### Response

    Status: 201 Created
    Content-Type: application/json

    {
        "id": 1,
        "name": "WMT",
        "description": "Walmart",
        "currentPrice": 121.7
        "lastUpdate": "2022-06-12T20:48:29.322934"
    }

### Update Stock Price

#### Request

`PUT /v1/api/stock/{id}`

    curl -X 'PUT' \
            'http://localhost:8080/v1/api/stock/1' \
            -H 'Content-Type: application/json' \
            -H 'Authorization: "Bearer <access_token>"' \
            -d '{
                "currentPrice": 128
            }'

#### Response

    Status: 200 OK
    Content-Type: application/json

    {
        "id": 1,
        "name": "WMT",
        "description": "Walmart",
        "currentPrice": 128
        "lastUpdate": "2022-06-12T21:48:29.322934"
    }

### Delete Stock

#### Request

`DELETE /v1/api/stock/{id}`

    curl -X 'DELETE' \
            -H 'Authorization: "Bearer <access_token>"' \
            'http://localhost:8080/v1/api/stock/1'

#### Response

    Status: 200 OK



## Stock Exchange APIs

### Add Stock to Stock Exchange

#### Request

`POST /v1/api/stock-exchange/{name}/stocks`

    curl -X 'POST' \
            'http://localhost:8080/v1/api/stock-exchange/Nasdaq/stocks' \
            -H 'Content-Type: application/json' \
            -H 'Authorization: "Bearer <access_token>"' \
            -d '[ 1 ]'

#### Response

    Status: 200 Ok
    Content-Type: application/json

    {
        "id": 1,
        "name": "Nasdaq",
        "description": "Nasdaq is a global electronic marketplace for buying and selling securities.",
        "liveInMarket": false,
        "stocks": [
              {
                "id": 1,
                "name": "WMT",
                "description": "Walmart",
                "currentPrice": 121.7
                "lastUpdate": "2022-06-12T20:48:29.322934"
              }
        ]
    }

### Load Stock Exchange Details

#### Request

`GET /v1/api/stock-exchange/{name}`

    curl -X 'POST' \
            -H 'Authorization: "Bearer <access_token>"' \
            'http://localhost:8080/v1/api/stock-exchange/Nasdaq'

#### Response

    Status: 200 Ok
    Content-Type: application/json

    {
        "id": 1,
        "name": "Nasdaq",
        "description": "Nasdaq is a global electronic marketplace for buying and selling securities.",
        "liveInMarket": false,
        "stocks": [
              {
                "id": 1,
                "name": "WMT",
                "description": "Walmart",
                "currentPrice": 121.7
                "lastUpdate": "2022-06-12T20:48:29.322934"
              }
        ]
    }


### Delete Stock from Stock Exchange

#### Request

`DELETE /v1/api/stock-exchange/{name}/stocks`

    curl -X 'DELETE' \
            'http://localhost:8080/v1/api/stock-exchange/Nasdaq/stocks' \
            -H 'Content-Type: application/json' \
            -H 'Authorization: "Bearer <access_token>"' \
            -d '[ 1 ]'

#### Response

    Status: 200 Ok