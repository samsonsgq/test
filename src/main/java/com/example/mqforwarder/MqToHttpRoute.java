package com.example.mqforwarder;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MqToHttpRoute extends RouteBuilder {
  private final String inputQueue;
  private final String targetSystemUrl;

  public MqToHttpRoute(
      @Value("${app.input-queue}") String inputQueue,
      @Value("${app.target-system-url}") String targetSystemUrl) {
    this.inputQueue = inputQueue;
    this.targetSystemUrl = targetSystemUrl;
  }

  @Override
  public void configure() {
    fromF("activemq:queue:%s", inputQueue)
        .routeId("mq-to-http-forwarder")
        .log("Forwarding MQ message to target system: ${body}")
        .setHeader("Content-Type", constant("application/json"))
        .toD("http://" + targetSystemUrl + "?bridgeEndpoint=true&throwExceptionOnFailure=true")
        .log("Target system responded with status ${header.CamelHttpResponseCode}");
  }
}
