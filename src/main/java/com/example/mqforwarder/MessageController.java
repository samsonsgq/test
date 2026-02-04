package com.example.mqforwarder;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> sendMessage(@RequestBody String payload) {
    String sanitized = payload == null ? "" : payload.trim();
    if (sanitized.isEmpty()) {
      return ResponseEntity.badRequest().body(Map.of("error", "payload must not be empty"));
    }
    messageService.sendToInput(sanitized);
    return ResponseEntity.status(HttpStatus.ACCEPTED)
        .body(Map.of("status", "queued", "payload", sanitized));
  }

  @GetMapping("/output")
  public Map<String, List<String>> readOutput(@RequestParam(name = "max", defaultValue = "5") int max) {
    int limited = Math.max(1, Math.min(max, 50));
    return Map.of("messages", messageService.receiveFromOutput(limited));
  }
}
