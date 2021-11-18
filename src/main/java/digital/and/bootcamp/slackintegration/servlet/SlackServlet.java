package digital.and.bootcamp.slackintegration.servlet;

import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackAppServlet;
import digital.and.bootcamp.slackintegration.model.AnswerRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.annotation.WebServlet;
import java.util.regex.Pattern;

@Slf4j
@WebServlet("/slack/events")
public class SlackServlet extends SlackAppServlet {

    public SlackServlet(App app) {
        super(app);

        app.blockAction(Pattern.compile("^submittedAnswer.+$"), (req, ctx) -> {
            String personId = req.getPayload().getUser().getId();
            String[] idSplit = req.getPayload().getActions().get(0).getValue().split("\\|");
            String question = idSplit[0];
            String surveyId = idSplit[1];
            String answer = req.getPayload().getActions().get(0).getText().getText();

            log.info("Received: {}, {}, {}, {}", surveyId, personId, question, answer);
            AnswerRequest answerRequest = AnswerRequest.builder()
                    .question(question)
                    .answer(answer)
                    .personId(personId)
                    .surveyId(surveyId)
                    .build();
            ResponseEntity<Void> response = new RestTemplate().postForEntity(System.getenv("ANSWERS_ENDPOINT"), answerRequest, Void.class);

            log.info("Response: {}", response.getStatusCodeValue());
            return ctx.ack();
        });
    }
}