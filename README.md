
Java + Spring boot + Neo4j
=======

## Technology Stack
##### This project was built using IntelliJ IDEA and uses the following technologies:
* Java 11 (OpenJDK 11.0.13)
* Spring Framework/Spring Boot version 2.7.4
* REST CRUD operations
* Swagger UI with Open API
* Spring DATA Neo4j
* Test coverage
    - Integration test
    - JUnit5
* Rest error handlers
* Locally installed Neo4j 5.1.1-community (username: neo4j, password: secret)
* Docker & docker-compose
* Postman

##  Setup

### Setup for local development
1. Open the project in Intellij and in the resources folder in the application.yaml set:
    * spring profiles active: local
2. Build the project
3. Open DockerDesktop
4. Inside the neo4j folder is the docker-compose. Run service neo4j-db.
5. Run an application from MovieAppApplication class
6. Test the API below using Postman or Swagger UI
7. Link to open Swagger UI:
    * http://localhost:8080/swagger-ui/index.html
8. Link to API docs:
    * http://localhost:8080/api-docs

### Setup for running application on other device
1. Install DockerDesktop
2. Pull tag from Docker repository docker with this command:
   ```http
    docker pull cosicdamnjan/movie-app:latest
    ```
3. Run docker compose file inside Neo4j folder or 
   - run next Docker command on your local machine from the correct location where docker-compose.yml file is:
    ```http
    Docker-compose up
    ```
4. Wait the application to start
5. Run Postman
6. Test the API below using Postman or Swagger UI
7. Link to open Swagger UI- http://localhost:8080/swagger-ui/index.html
8. Link to api docs http://localhost:8080/api-docss

## API Reference

### Movies

#### Find all movies

```http
  GET /api/movies
```

#### Find movie by id

```http
  GET /api/movies/${id}
```

#### Update movie

```http
  PUT /api/movies/${id}
```

#### Delete movie by id

```http
  DELETE /api/movies/${id}
```

#### Find all actors by movie id

```http
  GET /api/movies/${id}/actors
```

#### Search movie by name and genre

```http
  GET /api/movies/search?name=$name&genre=$genre
```

#### Add actor to movie

```http
  POST /api/movies/${id}/actor
```

#### Delete actor from movie

```http
  DELETE /api/movies/{movieId}/actors/{actorId}
```

#### Create review

```http
  POST /api/movies/{id}/reviews
```

#### Delete review

```http
  DELETE /api/movies/{id}/reviews/{reviewId}
```

#### Find all reviews by movie id

```http
  GET /api/movies/{id}/reviews
```

### Actors

#### Find all actors

```http
  GET /api/actors
```

#### Find actor by id

```http
  GET /api/actors/{id}
```

#### Create actpr

```http
  POST /api/actors
```

#### Update actor

```http
  PUT /api/actors/{id}
```

#### Delete actor

```http
  GET /api/actors/{id}
```

## Authors

### GitHub [cosicdamnjan](https://github.com/cosicdamnjan)
