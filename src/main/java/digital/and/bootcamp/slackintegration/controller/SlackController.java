package digital.and.bootcamp.slackintegration.controller;

import com.slack.api.Slack;
import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.users.UsersListRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(path = "/slack-integration")
public class SlackController {
    @Autowired
    App app;

    @PostMapping("/test")
    public ResponseEntity<Void> sendMessageToSlack() throws SlackApiException, IOException {
        Slack slack = Slack.getInstance();
        String token = System.getenv("BOT_TOKEN");
        UsersListResponse usersListResponse = slack.methods(token).usersList(UsersListRequest.builder().build());
        usersListResponse.getMembers().forEach(user -> {
            try {
                ChatPostMessageResponse response = slack.methods(token).chatPostMessage(req -> req
                        .channel(user.getId())
                        .text("Hi"));
                        log.info(response.getMessage().getText());

            } catch (IOException | SlackApiException e) {
                e.printStackTrace();
            }
        });

        return ResponseEntity.ok().build();
    }
}
