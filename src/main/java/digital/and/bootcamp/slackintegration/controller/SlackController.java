package digital.and.bootcamp.slackintegration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/slack")
public class SlackController {

    public ResponseEntity<Void> receiveInteraction() {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> sendMessageToSlack() {
        return ResponseEntity.ok().build();
    }
}
