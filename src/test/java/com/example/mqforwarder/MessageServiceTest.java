package com.example.mqforwarder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

  @Mock
  private ProducerTemplate producerTemplate;

  @Mock
  private ConsumerTemplate consumerTemplate;

  @InjectMocks
  private MessageService messageService;

  @Test
  void sendToInputPublishesToInputQueue() {
    messageService.sendToInput("payload");

    verify(producerTemplate).sendBody("activemq:queue:input", "payload");
  }

  @Test
  void receiveFromOutputStopsOnNullMessage() {
    when(consumerTemplate.receiveBody("activemq:queue:output", 200L, String.class))
        .thenReturn("first", null);

    List<String> messages = messageService.receiveFromOutput(5);

    assertThat(messages).containsExactly("first");
  }

  @Test
  void receiveFromOutputEnforcesMinimumOneMessage() {
    when(consumerTemplate.receiveBody("activemq:queue:output", 200L, String.class))
        .thenReturn("only");

    List<String> messages = messageService.receiveFromOutput(0);

    assertThat(messages).containsExactly("only");
  }
}
