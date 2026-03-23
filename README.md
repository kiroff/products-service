# Products Service

Spring Boot microservice for creating products and publishing a Kafka event for each product created.

## Features

- `POST /products` creates a product and emits a `ProductCreatedEvent`.
- Kafka topic `product-created-events-topic` is auto-created with 3 partitions and 3 replicas (min.insync.replicas=2).
- Open security by default (all requests permitted, CSRF disabled).

## Tech stack

- Java 25
- Spring Boot 4.0.3
- Spring Web MVC
- Spring Kafka
- Spring Security (permitAll)
- Maven

## Prerequisites

- JDK 25
- Maven 3.9+ (or `./mvnw`)
- Kafka (3 brokers by default)

## Configuration

Defaults are in `src/main/resources/application.yaml`:

- Server port: `8899`
- Kafka bootstrap servers: `host.docker.internal:9092,9094,9096`

If you are not running Kafka via Docker on your host, update the bootstrap servers accordingly.

## Run locally

Start Kafka (optional Docker compose included):

```bash
cd src/main/resources/static/docker
docker compose up -d
```

Run the service:

```bash
./mvnw spring-boot:run
```

## API

Create product:

```bash
curl -X POST "http://localhost:8899/products" \
  -H "Content-Type: application/json" \
  -d '{"title":"Camera","price":199.99,"quantity":5}'
```

Response:

```
Created product: id=<uuid>.
```

## Event payload

The service publishes `ProductCreatedEvent` to Kafka topic `product-created-events-topic` with:

```json
{
  "productId": "uuid",
  "title": "Camera",
  "price": 199.99,
  "quantity": 5
}
```
