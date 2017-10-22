## Dev Environment
  - JDK 1.8
  - RESTful: [Spring Boot](https://projects.spring.io/spring-boot/)
  - Gradle

## API Routing
POST /transactions

```sh
{
    "amount": 10.5,
    "timestamp": 1508706287956
}
```
GET /statistics
```sh
{
    "sum": 100,
    "avg": 10,
    "max": 20,
    "min": 5,
    "count": 10
}
```

## Running the tests
```sh
./gradlew clean test
```

## Starting the App
```
./gradlew clean bootRun
```
