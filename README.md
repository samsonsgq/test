# MQ Forwarder (Spring Boot + Apache Camel)

This project is a simple Spring Boot application that uses Apache Camel to read messages from an MQ queue and forward them to another system via HTTP.

## Prerequisites

- Java 17+
- Maven 3.9+
- An ActiveMQ broker (or compatible broker)

## Configure

Environment variables (defaults shown):

- `ACTIVEMQ_BROKER_URL` (default: `tcp://localhost:61616`)
- `ACTIVEMQ_USER` (default: `admin`)
- `ACTIVEMQ_PASSWORD` (default: `admin`)
- `INPUT_QUEUE` (default: `incoming.events`)
- `TARGET_SYSTEM_URL` (default: `localhost:8081/api/receive`)

## Run

```bash
mvn spring-boot:run
```

## How it works

The Camel route consumes from the configured ActiveMQ queue and forwards each message body to the configured target system URL over HTTP.
