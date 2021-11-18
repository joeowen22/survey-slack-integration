package digital.and.bootcamp.slackintegration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slack.api.Slack;
import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.users.UsersListRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.element.BlockElement;
import digital.and.bootcamp.slackintegration.model.SurveySendRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.asElements;
import static com.slack.api.model.block.element.BlockElements.button;

@Slf4j
@RestController
@RequestMapping(path = "/slack-integration")
public class SlackController {
    @Autowired
    App app;

    @PostMapping("/test")
    public ResponseEntity<Void> sendMessageToSlack(@RequestBody SurveySendRequest surveySendRequest) throws SlackApiException, IOException {
        Slack slack = Slack.getInstance();
        String token = System.getenv("BOT_TOKEN");

        UsersListResponse usersListResponse = slack.methods(token).usersList(UsersListRequest.builder().build());
        usersListResponse.getMembers().forEach(user -> {
            try {
                slack.methods(token)
                        .chatPostMessage(req -> req
                                .channel(user.getId())
                                .text("A survey has been sent")
                                .blocks(buildBlocks(surveySendRequest.getSurveyId(), surveySendRequest.getQuestions())));
            } catch (IOException | SlackApiException e) {
                log.error(e.getMessage());
            }
        });

        return ResponseEntity.ok().build();
    }

    public List<LayoutBlock> buildBlocks(String surveyId, List<String> questions) {
        List<LayoutBlock> blocks = new ArrayList<>();
        blocks.add(section(section -> section.text(markdownText("Please answer the following questions:"))));
        blocks.add(divider());
        questions.forEach(question -> {
            blocks.add(section(section -> section.text(markdownText(question))));
            blocks.add(divider());
            blocks.add(actions(
                    actions ->
                            actions.elements(
                                getAnswers(question, surveyId)
                            )
            ));
        });
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info(objectMapper.writeValueAsString(blocks));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return blocks;
    }

    public List<BlockElement> getAnswers(String question, String surveyId) {
        List<BlockElement> answers =  new ArrayList<>();
        Arrays.asList("Strongly Disagree", "Disagree", "Neutral", "Agree", "Strongly Agree").forEach(value -> {
            answers.add(
                    button(button -> button.text(plainText(value)).actionId(question+"|"+surveyId).value(value))
            );
        });
        return answers;
    }
}
