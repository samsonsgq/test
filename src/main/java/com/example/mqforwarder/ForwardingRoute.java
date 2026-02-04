package com.example.mqforwarder;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ForwardingRoute extends RouteBuilder {

  @Override
  public void configure() {
    from("activemq:queue:input")
        .routeId("forward-input-to-output")
        .log("Forwarding message from input to output queue: ${body}")
        .to("activemq:queue:output");
  }
}
