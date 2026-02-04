package com.example.mqforwarder;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
  private static final String INPUT_QUEUE = "activemq:queue:input";
  private static final String OUTPUT_QUEUE = "activemq:queue:output";
  private static final Duration DEFAULT_TIMEOUT = Duration.ofMillis(200);

  private final ProducerTemplate producerTemplate;
  private final ConsumerTemplate consumerTemplate;

  public MessageService(ProducerTemplate producerTemplate, ConsumerTemplate consumerTemplate) {
    this.producerTemplate = producerTemplate;
    this.consumerTemplate = consumerTemplate;
  }

  public void sendToInput(String payload) {
    producerTemplate.sendBody(INPUT_QUEUE, payload);
  }

  public List<String> receiveFromOutput(int maxMessages) {
    List<String> messages = new ArrayList<>();
    int remaining = Math.max(1, maxMessages);
    for (int i = 0; i < remaining; i++) {
      String message = consumerTemplate.receiveBody(OUTPUT_QUEUE, DEFAULT_TIMEOUT.toMillis(), String.class);
      if (message == null) {
        break;
      }
      messages.add(message);
    }
    return messages;
  }
}
